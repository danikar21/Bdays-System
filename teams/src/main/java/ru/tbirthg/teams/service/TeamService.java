package ru.tbirthg.teams.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tbirthg.teams.dto.TeamDto;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    public List<TeamDto> getAllTeams() {
        return List.of(
                new TeamDto(1L, "Разработка"),
                new TeamDto(2L, "Маркетинг")
        );
    }

    public TeamDto getTeamById(Long id) {
        return new TeamDto(id, "Команда " + id);
    }

    @Transactional
    public TeamDto createTeam(TeamDto team) {
        return new TeamDto(99L, team.getName());
    }

    @Transactional
    public TeamDto updateTeam(Long id, TeamDto teamDto) {
        teamDto.setId(id);
        return teamDto;
    }

    @Transactional
    public void deleteTeam(Long id) {
    }

//    public List<UserDto> getTeamMembers(Long teamId) {
//        return List.of(
//                new UserDto("Test", "Testovich", LocalDate.of(2005, 2, 15)),
//                new UserDto("Testa", "Testovichna", LocalDate.of(2006, 3, 30))
//        );
//    }

    @Transactional
    public void removeAllMembersFromTeam(Long teamId) {
    }

    @Transactional
    public void removeMemberFromTeam(Long teamId, Long userId) {
    }
}