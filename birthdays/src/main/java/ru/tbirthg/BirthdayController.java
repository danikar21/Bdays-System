package ru.tbirthg;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Получить список ближайших дней рождения (30 дней), только для USER и ADMIN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = BirthdayDto.class)))),
            @ApiResponse(responseCode = "401", description = "Не авторизован (отсутствует или недействителен JWT)", content = @Content),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен (недостаточно прав)", content = @Content)
    })
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<BirthdayDto> getUpcomingBirthdays() {
        return birthdayService.getUpcomingBirthdays();
    }
}