package ru.tbirthg.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.tbirthg.users.dto.TeamRequestDto;
import ru.tbirthg.users.dto.TeamResponseDto;
import ru.tbirthg.users.dto.UserResponseDto;
import ru.tbirthg.users.service.TeamService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teams")
@Tag(name = "Команды")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @GetMapping
    @Operation(summary = "Получить список всех команд (только для USER и ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TeamResponseDto.class)))),
            @ApiResponse(responseCode = "401", description = "Не авторизован (отсутствует или недействителен JWT)",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен (недостаточно прав)",
                    content = @Content)
    })
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<TeamResponseDto> getAllTeams() {
        return teamService.getAllTeams();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить информацию о команде по ID (только для USER и ADMIN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ",
                    content = @Content(schema = @Schema(implementation = TeamResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен (требуется роль USER или ADMIN)",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Команда не найдена",
                    content = @Content)
    })
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public TeamResponseDto getTeamById(@PathVariable Long id) {
        return teamService.getTeamById(id);
    }

    @PostMapping
    @Operation(summary = "Создать новую команду (только для ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Успешный ответ",
                    content = @Content(schema = @Schema(implementation = TeamResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные (например, пустое название)",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав (требуется роль ADMIN)",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Команда с таким названием уже существует",
                    content = @Content)
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public TeamResponseDto createTeam(@Valid @RequestBody TeamRequestDto teamRequestDto) {
        return teamService.createTeam(teamRequestDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить команду (только для ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ",
                    content = @Content(schema = @Schema(implementation = TeamResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав (требуется роль ADMIN)",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Команда не найдена",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Название команды уже занято",
                    content = @Content)
    })
    @PreAuthorize("hasRole('ADMIN')")
    public TeamResponseDto updateTeam(@PathVariable Long id, @Valid @RequestBody TeamRequestDto teamRequestDto) {
        return teamService.updateTeam(id, teamRequestDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить команду, если в ней нет сотрудников (только для ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Успешный ответ"),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав (требуется роль ADMIN)",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Команда не найдена",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Команда не может быть удалена, так как содержит сотрудников",
                    content = @Content)
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/members")
    @Operation(summary = "Получить список сотрудников, входящих в команду")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponseDto.class)))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен (требуется роль USER или ADMIN)",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Команда не найдена",
                    content = @Content)
    })
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<UserResponseDto> getTeamMembers(@PathVariable Long id) {
        return teamService.getTeamMembers(id);
    }

    @DeleteMapping("/{teamId}/members/{userId}")
    @Operation(summary = "Удалить сотрудника из команды (только для ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Успешный ответ"),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав (требуется роль ADMIN)",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Команда или сотрудник не найдены",
                    content = @Content)
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeMemberFromTeam(@PathVariable Long teamId, @PathVariable Long userId) {
        teamService.removeMemberFromTeam(teamId, userId);
        return ResponseEntity.noContent().build();
    }
}