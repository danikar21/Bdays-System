package ru.tbirthg.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.tbirthg.users.entity.RefreshTokenEntity;
import ru.tbirthg.users.entity.UserEntity;
import ru.tbirthg.users.repository.RefreshTokenRepository;
import ru.tbirthg.users.repository.UserRepository;

import java.time.Instant;
import java.util.Optional;

@Service
@ConditionalOnProperty(name = "refresh-token.store", havingValue = "jpa", matchIfMissing = true)
public class JpaRefreshTokenStore implements RefreshTokenStore {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public JpaRefreshTokenStore(RefreshTokenRepository refreshTokenRepository,
                                UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void save(String token, String userEmail, long ttlSeconds) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        RefreshTokenEntity refreshToken = new RefreshTokenEntity();
        refreshToken.setToken(token);
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusSeconds(ttlSeconds));
        refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Optional<String> findUserByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .map(rt -> rt.getUser().getEmail());
    }

    @Override
    public void delete(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(refreshTokenRepository::delete);
    }
}