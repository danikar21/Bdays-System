package ru.tbirthg.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Запрос на вход")
public class LoginRequest {
    @Schema(description = "Email пользователя", example = "email@example.com")
    @NotBlank
    @Email
    private String email;
    @Schema(description = "Пароль", example = "password123")
    @NotBlank
    private String password;
}

