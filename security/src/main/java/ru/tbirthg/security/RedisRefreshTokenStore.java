package ru.tbirthg.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@ConditionalOnProperty(name = "refresh-token.store", havingValue = "redis")
public class RedisRefreshTokenStore implements RefreshTokenStore {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisRefreshTokenStore(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(String token, String userEmail, long ttlSeconds) {
        redisTemplate.opsForValue().set(token, userEmail, Duration.ofSeconds(ttlSeconds));
    }

    @Override
    public Optional<String> findUserByToken(String token) {
        String userEmail = redisTemplate.opsForValue().get(token);
        return Optional.ofNullable(userEmail);
    }

    @Override
    public void delete(String token) {
        redisTemplate.delete(token);
    }
}