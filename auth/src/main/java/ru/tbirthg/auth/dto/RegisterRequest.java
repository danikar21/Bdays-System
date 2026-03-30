package ru.tbirthg.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на регистрацию")
public class RegisterRequest {
    @Schema(description = "Email", example = "email@example.com")
    @NotBlank
    @Email
    private String email;
    @Schema(description = "Пароль (минимум 8 символов)", example = "password123")
    @Size(min = 8)
    @Pattern(regexp = ".*\\d.*", message = "Пароль должен содержать хотя бы одну цифру")
    @NotBlank
    private String password;
}