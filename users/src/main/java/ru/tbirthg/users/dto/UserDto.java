package ru.tbirthg.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tbirthg.common.enums.RoleType;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO пользователя")
public class UserDto {
    @Schema(description = "Идентификатор пользователя")
    private Long id;
    @Schema(description = "Email пользователя (Уникальный)")
    @Email
    @NotBlank
    private String email;
    @Schema(description = "Имя")
    private String firstName;
    @Schema(description = "Отчество")
    private String patronymic;
    @Schema(description = "Фамилия")
    private String lastName;
    @Schema(description = "День рождения")
    private LocalDate birthDate;
    @Schema(description = "Должность")
    private String position;
    @Schema(description = "Роль")
    private RoleType role;

    public UserDto(Long id, String email, String firstName, String lastName, LocalDate birthDate, String position) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.position = position;
    }

    public UserDto(String firstName, String lastName, LocalDate birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }
}
