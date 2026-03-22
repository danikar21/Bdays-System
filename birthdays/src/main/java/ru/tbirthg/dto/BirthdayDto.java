package ru.tbirthg.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BirthdayDto {
    private Long userId;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
}