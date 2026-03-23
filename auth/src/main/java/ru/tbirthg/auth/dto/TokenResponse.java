package ru.tbirthg.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ с токенами аутентификации")
public class TokenResponse {
    @Schema(description = "Access Token")
    private String accessToken;
    @Schema(description = "Refresh Token")
    private String refreshToken;
    private String tokenType = "Bearer";
}