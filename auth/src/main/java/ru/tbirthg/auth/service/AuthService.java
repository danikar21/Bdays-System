package ru.tbirthg.auth.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.tbirthg.auth.dto.LoginRequest;
import ru.tbirthg.auth.dto.RegisterRequest;
import ru.tbirthg.auth.dto.TokenResponse;
import ru.tbirthg.common.enums.RoleType;
import ru.tbirthg.security.JwtService;
import ru.tbirthg.security.RefreshTokenStore;
import ru.tbirthg.users.entity.UserEntity;
import ru.tbirthg.users.repository.UserRepository;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenStore refreshTokenStore;

    private static final String REFRESH_TOKEN_COOKIE = "refresh_token";

    @Transactional
    public TokenResponse login(LoginRequest request, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String email = authentication.getName();

            UserEntity user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

            String accessToken = jwtService.generateAccessToken(email);
            String refreshToken = jwtService.generateRefreshToken(email);

            refreshTokenStore.save(refreshToken, email, jwtService.getRefreshTokenExpiration() / 1000);

            Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE, refreshToken);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/api/v1/auth");
            cookie.setMaxAge((int) (jwtService.getRefreshTokenExpiration() / 1000));
            response.addCookie(cookie);
            return new TokenResponse(accessToken, null, "Bearer");
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }

    @Transactional
    public TokenResponse register(RegisterRequest request, HttpServletResponse response) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(RoleType.GUEST);
        user.setCreatedAt(OffsetDateTime.now());
        user.setActive(true);
        userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        refreshTokenStore.save(refreshToken, user.getEmail(), jwtService.getRefreshTokenExpiration() / 1000);

        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE, refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/api/v1/auth");
        cookie.setMaxAge((int) (jwtService.getRefreshTokenExpiration() / 1000));
        response.addCookie(cookie);

        return new TokenResponse(accessToken, null, "Bearer");
    }

    @Transactional
    public TokenResponse refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractTokenFromCookie(request);
        if (refreshToken == null || !jwtService.validateRefreshToken(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }

        String userEmail = refreshTokenStore.findUserByToken(refreshToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token not found"));

        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        String newAccessToken = jwtService.generateAccessToken(userEmail);
        String newRefreshToken = jwtService.generateRefreshToken(userEmail);

        refreshTokenStore.delete(refreshToken);
        refreshTokenStore.save(newRefreshToken, userEmail, jwtService.getRefreshTokenExpiration() / 1000);

        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE, newRefreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/api/v1/auth");
        cookie.setMaxAge((int) (jwtService.getRefreshTokenExpiration() / 1000));
        response.addCookie(cookie);

        return new TokenResponse(newAccessToken, null, "Bearer");
    }

    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractTokenFromCookie(request);
        if (refreshToken != null) {
            refreshTokenStore.delete(refreshToken);
        }

        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/api/v1/auth");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (REFRESH_TOKEN_COOKIE.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}