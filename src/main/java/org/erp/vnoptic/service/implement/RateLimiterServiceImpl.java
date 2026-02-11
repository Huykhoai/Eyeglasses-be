package org.erp.vnoptic.service.implement;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.erp.vnoptic.service.RateLimiterService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RateLimiterServiceImpl implements RateLimiterService {

    private final StringRedisTemplate redisTemplate;

    private static final String LUA_SCRIPT = "local key = KEYS[1] " +
            "local limit = tonumber(ARGV[1]) " +
            "local window = tonumber(ARGV[2]) " +
            "local current = redis.call('INCR', key) " +
            "if current == 1 then " +
            "    redis.call('EXPIRE', key, window) " +
            "end " +
            "return current <= limit";

    /**
     * Checks if a request is allowed.
     *
     * @param key             Unique key (IP address)
     * @param limit           Max requests allowed
     * @param windowInSeconds Time window in seconds
     * @return true if allowed, false if rate limited
     */
    @Override
    public boolean isAllowed(String key, int limit, int windowInSeconds) {
        String redisKey = "rate_limit:" + key;
        DefaultRedisScript<Boolean> script = new DefaultRedisScript<>(LUA_SCRIPT, Boolean.class);
        return redisTemplate.execute(script, Collections.singletonList(redisKey),
                String.valueOf(limit), String.valueOf(windowInSeconds));
    }

    @Override
    public boolean isBlacklisted(String ip) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:" + ip));
    }

    @Override
    public void blacklistIp(String ip, long durationInSeconds) {
        redisTemplate.opsForValue().set("blacklist:" + ip, "true", java.time.Duration.ofSeconds(durationInSeconds));
    }

    @Override
    public long incrementViolations(String ip, int windowInSeconds) {
        String key = "violations:" + ip;
        Long count = redisTemplate.opsForValue().increment(key);
        if (count != null && count == 1) {
            redisTemplate.expire(key, java.time.Duration.ofSeconds(windowInSeconds));
        }
        return count != null ? count : 0;
    }
}
