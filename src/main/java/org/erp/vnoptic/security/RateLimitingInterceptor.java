package org.erp.vnoptic.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.erp.vnoptic.exception.RateLimitException;
import org.erp.vnoptic.service.RateLimiterService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class RateLimitingInterceptor implements HandlerInterceptor {

    private final RateLimiterService rateLimiterService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        try {
            String clientIp = request.getRemoteAddr();

            if (rateLimiterService.isBlacklisted(clientIp)) {
                throw new RateLimitException(
                        "Your IP has been temporarily blocked for 30 minutes due to multiple violations.");
            }

            if (request.getRequestURI().contains("/api/auth/login")) {
                boolean allowed = rateLimiterService.isAllowed(clientIp + ":login", 5, 60);
                if (!allowed) {
                    handleViolation(clientIp, "Too many login attempts. Please try again later.");
                    return false;
                }
            }

            boolean allowed = rateLimiterService.isAllowed(clientIp, 100, 60);
            if (!allowed) {
                handleViolation(clientIp, "Global rate limit exceeded.");
                return false;
            }

            return true;
        } catch (RateLimitException e) {
            throw new RateLimitException(e.getMessage());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    private void handleViolation(String ip, String message) {
        long violations = rateLimiterService.incrementViolations(ip, 300);

        if (violations >= 5) {
            rateLimiterService.blacklistIp(ip, 1800);
            throw new RateLimitException("Multiple violations detected. Your IP is now blocked for 30 minutes.");
        } else {
            throw new RateLimitException(message);
        }
    }
}
