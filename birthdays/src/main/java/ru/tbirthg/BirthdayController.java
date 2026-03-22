package ru.tbirthg;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tbirthg.dto.BirthdayDto;
import ru.tbirthg.service.BirthdayService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/birthdays")
@Tag(name = "Дни рождения")
@RequiredArgsConstructor
public class BirthdayController {

    private final BirthdayService birthdayService;

    @GetMapping("/upcoming")
    @Operation(summary = "Список ближайших дней рождения (30 дней)")
    @PreAuthorize("isAuthenticated()")
    public List<BirthdayDto> getUpcomingBirthdays() {
        return birthdayService.getUpcomingBirthdays();
    }
}