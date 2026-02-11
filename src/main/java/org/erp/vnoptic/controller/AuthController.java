package org.erp.vnoptic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.erp.vnoptic.common.ApiResponse;
import org.erp.vnoptic.requests.LoginRequest;
import org.erp.vnoptic.requests.RefreshTokenRequest;
import org.erp.vnoptic.requests.RegisterRequest;
import org.erp.vnoptic.responese.AuthResponse;
import org.erp.vnoptic.security.JwtTokenProvider;
import org.erp.vnoptic.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user login and token management")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    @Operation(summary = "Login to get JWT token and user info")
    public ApiResponse<AuthResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        AuthResponse authResponse = authService.login(request);
        setRefreshTokenCookie(response, authResponse.getRefreshToken());
        authResponse.setRefreshToken(null); // Do not send in body
        return ApiResponse.success("Login successful", authResponse);
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user account")
    public ApiResponse<String> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ApiResponse.success("User registered successfully", null);
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh JWT token using refresh token from cookie")
    public ApiResponse<AuthResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = Arrays.stream(request.getCookies() != null ? request.getCookies() : new Cookie[0])
                .filter(cookie -> "refresh_token".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Refresh token missing"));

        AuthResponse authResponse = authService.refreshToken(new RefreshTokenRequest(refreshToken));
        setRefreshTokenCookie(response, authResponse.getRefreshToken());
        authResponse.setRefreshToken(null);
        return ApiResponse.success("Token refreshed successfully", authResponse);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout and invalidate current token")
    public ApiResponse<String> logout(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader("Authorization");
        authService.logout(authHeader);
        clearRefreshTokenCookie(response);
        return ApiResponse.success("Logged out successfully", null);
    }

    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(false) // Set to true in production with HTTPS
                .path("/")
                .maxAge(jwtTokenProvider.getRefreshExpiration() / 1000)
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private void clearRefreshTokenCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
