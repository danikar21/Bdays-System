package ru.tbirthg.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tbirthg.auth.dto.LoginRequest;
import ru.tbirthg.auth.dto.RefreshRequest;
import ru.tbirthg.auth.dto.RegisterRequest;
import ru.tbirthg.auth.dto.TokenResponse;
import ru.tbirthg.auth.service.AuthService;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация", description = "Самостоятельная регистрация (для гостя), вход, обновление токенов")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Вход в систему", description = "Аутентификация по email и паролю. Возвращает access и refresh токены")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная аутентификация",
                    content = @Content(schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "400", description = "Неверный формат запроса (ошибка валидации, некорректные данные)",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Неверные учетные данные (email или пароль)",
                    content = @Content)
    })
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    @Operation(summary = "Самостоятельная регистрация (только для GUEST)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно создан",
                    content = @Content(schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные (пароль < 6 символов)",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Пользователь с таким email уже существует",
                    content = @Content)
    })
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Обновление пары токенов",
            description = "Принимает валидный refresh токен и выдает новую пару токенов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Токены успешно обновлены",
                    content = @Content(schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "400", description = "Неверный формат запроса",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Невалидный refresh токен",
                    content = @Content)
    })
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        return ResponseEntity.ok(authService.refresh(request.getRefreshToken()));
    }
}