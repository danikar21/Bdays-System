package ru.tbirthg.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Информация о команде")
public class TeamResponseDto {
    @Schema(description = "ID команды")
    private Long id;
    @Schema(description = "Название команды")
    @NotBlank
    private String name;
}