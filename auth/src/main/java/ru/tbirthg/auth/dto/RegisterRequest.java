package ru.tbirthg.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на регистрацию")
public class RegisterRequest {
    @Schema(description = "Email")
    @NotBlank
    @Email
    private String email;
    @Schema(description = "Пароль (минимум 6 символов)")
    @NotBlank
    @Size(min = 6)
    private String password;
}