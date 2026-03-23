package ru.tbirthg.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tbirthg.dto.BirthdayDto;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BirthdayService {

    public List<BirthdayDto> getUpcomingBirthdays() {
        return List.of(
                new BirthdayDto(3L, "Ivan", "Ivanov", LocalDate.of(2006, 3, 30)),
                new BirthdayDto(4L, "Petr", "Petrov", LocalDate.of(2001, 3, 28)),
                new BirthdayDto(5L, "Svetlana", "Sidorova", LocalDate.of(2003, 3, 20)),
                new BirthdayDto(6L, "Vasilisa", "Vasechkina", LocalDate.of(1999, 3, 15)),
                new BirthdayDto(7L, "Anatoliy", "Sidorov", LocalDate.of(2008, 3, 7))
        );
    }
}