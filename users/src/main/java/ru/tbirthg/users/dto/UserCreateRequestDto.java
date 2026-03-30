package ru.tbirthg.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на создание пользователя (только для ADMIN)")
public class UserCreateRequestDto {

    @Schema(description = "Фамилия")
    @NotBlank
    private String lastName;

    @Schema(description = "Имя")
    @NotBlank
    private String firstName;

    @Schema(description = "Отчество")
    private String patronymic;

    @Schema(description = "День рождения")
    @NotNull
    @Past
    private LocalDate birthDate;

    @Schema(description = "Email пользователя (Уникальный)", example = "email@example.com")
    @Email
    @NotBlank
    private String email;

    @Schema(description = "ID команды (не должен быть пустым)")
    @NotNull
    private Long teamId;

    @Schema(description = "Должность")
    @NotBlank
    private String position;

    @Schema(description = "Пароль (минимум 8 символов)", example = "password123")
    @Size(min = 8)
    @Pattern(regexp = ".*\\d.*", message = "Пароль должен содержать хотя бы одну цифру")
    @NotBlank
    private String password;
}
