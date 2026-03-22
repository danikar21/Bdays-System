package ru.tbirthg.teams;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.tbirthg.teams.dto.TeamDto;
import ru.tbirthg.teams.service.TeamService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teams")
@Tag(name = "Команды")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @GetMapping
    @Operation(summary = "Список всех команд")
    public List<TeamDto> getAllTeams() {
        return teamService.getAllTeams();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Детальная информация о команде")
    public TeamDto getTeamById(@PathVariable Long id) {
        return teamService.getTeamById(id);
    }

    @PostMapping
    @Operation(summary = "Создать новую команду")
    @PreAuthorize("hasRole('ADMIN')")
    public TeamDto createTeam(@RequestBody TeamDto team) {
        return teamService.createTeam(team);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить команду (только для ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    public TeamDto updateTeam(@PathVariable Long id, @RequestBody TeamDto teamDto) {
        return teamService.updateTeam(id, teamDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить команду, если в ней нет сотрудников (только для ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/{id}/members")
//    @Operation(summary = "Получить список сотрудников, входящих в команду")
//    public List<UserDto> getTeamMembers(@PathVariable Long id) {
//        return teamService.getTeamMembers(id);
//    }

    @DeleteMapping("/{teamId}/members")
    @Operation(summary = "Удалить всех сотрудников из команды (только для ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeAllMembersFromTeam(@PathVariable Long teamId) {
        teamService.removeAllMembersFromTeam(teamId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{teamId}/members/{userId}")
    @Operation(summary = "Удалить сотрудника из команды (только для ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeMemberFromTeam(@PathVariable Long teamId, @PathVariable Long userId) {
        teamService.removeMemberFromTeam(teamId, userId);
        return ResponseEntity.noContent().build();
    }
}