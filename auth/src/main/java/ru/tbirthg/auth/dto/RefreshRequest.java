package ru.tbirthg.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Запрос на обновление токенов")
public class RefreshRequest {
    @Schema(description = "Refresh токен")
    @NotBlank
    private String refreshToken;
}