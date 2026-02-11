package org.erp.vnoptic.service.implement;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import lombok.experimental.FieldDefaults;
import org.erp.vnoptic.entity.Account;
import org.erp.vnoptic.entity.Employee;
import org.erp.vnoptic.repository.AccountRepository;
import org.erp.vnoptic.repository.EmployeeRepository;
import org.erp.vnoptic.repository.RoleFeatureRepository;
import org.erp.vnoptic.requests.LoginRequest;
import org.erp.vnoptic.requests.RefreshTokenRequest;
import org.erp.vnoptic.requests.RegisterRequest;
import org.erp.vnoptic.responese.AuthResponse;
import org.erp.vnoptic.security.JwtTokenProvider;
import org.erp.vnoptic.security.UserPrincipal;
import org.erp.vnoptic.service.AuthService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {
    AuthenticationManager authenticationManager;
    UserDetailsService userDetailsService;
    JwtTokenProvider jwtTokenProvider;
    RoleFeatureRepository roleFeatureRepository;
    PasswordEncoder passwordEncoder;
    EmployeeRepository employeeRepository;
    AccountRepository accountRepository;
    StringRedisTemplate redisTemplate;

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(),
                            request.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect password");
        }

        UserPrincipal principal = (UserPrincipal) userDetailsService.loadUserByUsername(request.getUsername());

        String token = jwtTokenProvider.generateToken(principal);
        String refreshToken = jwtTokenProvider.generateRefreshToken(principal);

        List<String> roles = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        List<String> features = principal.getRoleIds().isEmpty() ? Collections.emptyList()
                : roleFeatureRepository.findFeatureUrlsByRoleIds(principal.getRoleIds());

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .username(principal.getUsername())
                .email(principal.getEmail())
                .roles(roles)
                .features(features)
                .build();
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String username = jwtTokenProvider.extractUsername(request.getRefreshToken());
        UserPrincipal principal = (UserPrincipal) userDetailsService.loadUserByUsername(username);

        if (Boolean.TRUE.equals(jwtTokenProvider.validateToken(request.getRefreshToken(), principal))) {
            String newToken = jwtTokenProvider.generateToken(principal);
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(principal);

            List<String> roles = principal.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            List<String> features = principal.getRoleIds().isEmpty() ? Collections.emptyList()
                    : roleFeatureRepository.findFeatureUrlsByRoleIds(principal.getRoleIds());

            return AuthResponse.builder()
                    .token(newToken)
                    .refreshToken(newRefreshToken)
                    .username(principal.getUsername())
                    .email(principal.getEmail())
                    .roles(roles)
                    .features(features)
                    .build();
        }
        throw new RuntimeException("Invalid Refresh Token");
    }

    @Override
    public void logout(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            long expiration = jwtTokenProvider.getRemainingExpiration(jwt);
            if (expiration > 0) {
                redisTemplate.opsForValue().set("blacklist_token:" + jwt, "true", expiration,
                        TimeUnit.MILLISECONDS);
            }
        }
    }

    @Override
    public void register(RegisterRequest request) {
        try {
            if (accountRepository.findByUsernameOrEmail(request.getUsername()).isPresent()) {
                throw new NoSuchElementException("Username already exists");
            }

            Employee employee = employeeRepository.findById(request.getEmployeeId())
                    .orElseThrow(() -> new NoSuchElementException("Employee not found"));

            Account account = new Account();
            account.setUsername(request.getUsername());
            account.setEmail(request.getEmail());
            account.setPassword(passwordEncoder.encode(request.getPassword()));
            account.setEmployee(employee);
            account.setDisContinue(false);
            accountRepository.save(account);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
