package org.erp.vnoptic.security;

import lombok.RequiredArgsConstructor;
import org.erp.vnoptic.entity.Account;
import org.erp.vnoptic.repository.AccountRepository;
import org.erp.vnoptic.repository.AccountRoleRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final AccountRoleRepository accountRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsernameOrEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        List<String> roles = accountRoleRepository.findRoleNamesByAccountId(account.getId());
        List<Integer> roleIds = accountRoleRepository.findRoleIdsByAccountId(account.getId());

        return new UserPrincipal(account, roles, roleIds);
    }
}
