package ru.tbirthg.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.tbirthg.users.dto.TeamRequestDto;
import ru.tbirthg.users.dto.TeamResponseDto;
import ru.tbirthg.users.dto.UserResponseDto;
import ru.tbirthg.users.entity.TeamEntity;
import ru.tbirthg.users.entity.UserEntity;
import ru.tbirthg.users.repository.TeamRepository;
import ru.tbirthg.users.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public List<TeamResponseDto> getAllTeams() {
        return teamRepository.findAll().stream().map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public TeamResponseDto getTeamById(Long id) {
        TeamEntity team = teamRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found"));
        return mapToDto(team);
    }

    @Transactional
    public TeamResponseDto createTeam(TeamRequestDto teamRequestDto) {
        if (teamRepository.findByNameIgnoreCase(teamRequestDto.getName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Team with this name already exists");
        }
        TeamEntity team = new TeamEntity();
        team.setName(teamRequestDto.getName());
        TeamEntity saved = teamRepository.save(team);
        return mapToDto(saved);
    }

    @Transactional
    public TeamResponseDto updateTeam(Long id, TeamRequestDto teamRequestDto) {
        TeamEntity team = teamRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found"));

        if (!team.getName().equalsIgnoreCase(teamRequestDto.getName())) {
            if (teamRepository.findByNameIgnoreCase(teamRequestDto.getName()).isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Team with this name already exists");
            }
            team.setName(teamRequestDto.getName());
        }
        TeamEntity updated = teamRepository.save(team);
        return mapToDto(updated);
    }


    @Transactional
    public void deleteTeam(Long id) {
        if (!teamRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found");
        }
        if (userRepository.existsByTeamId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Team contains members");
        }
        teamRepository.deleteById(id);
    }

    public List<UserResponseDto> getTeamMembers(Long teamId) {
        if (!teamRepository.existsById(teamId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found");
        }
        List<UserEntity> members = userRepository.findByTeamId(teamId);
        return members.stream()
                .map(this::mapToUserResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeMemberFromTeam (Long teamId, Long userId){
        if (!teamRepository.existsById(teamId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found");
        }
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (user.getTeam() == null || !user.getTeam().getId().equals(teamId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not a member of this team");
        }
        user.setTeam(null);
        userRepository.save(user);
    }

    private TeamResponseDto mapToDto(TeamEntity entity) {
        return new TeamResponseDto(entity.getId(), entity.getName());
    }

    private UserResponseDto mapToUserResponseDto(UserEntity entity) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setFirstName(entity.getFirstName());
        dto.setPatronymic(entity.getPatronymic());
        dto.setLastName(entity.getLastName());
        dto.setBirthDate(entity.getBirthDate());
        dto.setPosition(entity.getPosition());
        dto.setRole(entity.getRole());
        if (entity.getTeam() != null) {
            dto.setTeamId(entity.getTeam().getId());
            dto.setTeamName(entity.getTeam().getName());
        }
        return dto;
    }
}