package ru.tbirthg.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tbirthg.common.enums.RoleType;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на изменение пользователя (только для ADMIN)")
public class UserUpdateRequestDto {

    @Schema(description = "Фамилия")
    private String lastName;

    @Schema(description = "Имя")
    private String firstName;

    @Schema(description = "Отчество")
    private String patronymic;

    @Schema(description = "День рождения")
    @Past
    private LocalDate birthDate;

    @Schema(description = "Email пользователя (Уникальный)", example = "email@example.com")
    @Email
    private String email;

    @Schema(description = "ID команды")
    private Long teamId;

    @Schema(description = "Должность")
    private String position;

    @Schema(description = "Пароль (минимум 8 символов)", example = "password123")
    @Size(min = 8)
    @Pattern(regexp = ".*\\d.*", message = "Пароль должен содержать хотя бы одну цифру")
    private String password;

    @Schema(description = "Роль")
    private RoleType role;
}
