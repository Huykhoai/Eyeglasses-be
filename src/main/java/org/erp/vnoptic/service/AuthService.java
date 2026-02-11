package org.erp.vnoptic.service;


import org.erp.vnoptic.requests.LoginRequest;
import org.erp.vnoptic.requests.RefreshTokenRequest;
import org.erp.vnoptic.requests.RegisterRequest;
import org.erp.vnoptic.responese.AuthResponse;


public interface AuthService {
        AuthResponse login(LoginRequest request);

        AuthResponse refreshToken(RefreshTokenRequest request);

        void logout(String token);

        void register(RegisterRequest request);

}
