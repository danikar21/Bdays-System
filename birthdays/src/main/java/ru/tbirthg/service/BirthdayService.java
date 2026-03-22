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
                new BirthdayDto(2L, "Testa", "Testovichna", LocalDate.of(2006, 3, 30)),
                new BirthdayDto(3L, "User", "Userovich", LocalDate.of(2001, 3, 28))
        );
    }
}