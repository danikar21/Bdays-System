package ru.tbirthg.security;

import java.util.Optional;

public interface RefreshTokenStore {
    void save(String token, String userEmail, long ttlSeconds);
    Optional<String> findUserByToken(String token);
    void delete(String token);
}