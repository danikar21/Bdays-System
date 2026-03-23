package ru.tbirthg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Информация о дне рождения")
public class BirthdayDto {
    @Schema(description = "ID пользователя")
    private Long userId;
    @Schema(description = "Имя")
    private String firstName;
    @Schema(description = "Фамилия")
    private String lastName;
    @Schema(description = "День рождения")
    private LocalDate birthDate;
}