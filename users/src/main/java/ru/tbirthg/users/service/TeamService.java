package ru.tbirthg.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.tbirthg.users.dto.TeamDto;
import ru.tbirthg.users.dto.UserDto;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final UserService userService;

    private final Map<Long, TeamDto> teams = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(3);

    private final Map<Long, Set<Long>> teamMembers = new ConcurrentHashMap<>();

    {
        teams.put(1L, new TeamDto(1L, "Development"));
        teams.put(2L, new TeamDto(2L, "Marketing"));
        teamMembers.put(1L, ConcurrentHashMap.newKeySet());
        teamMembers.put(2L, ConcurrentHashMap.newKeySet());
    }

    public List<TeamDto> getAllTeams() {
        return new ArrayList<>(teams.values());
    }

    public TeamDto getTeamById(Long id) {
        TeamDto team = teams.get(id);
        if (team == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found");
        }
        return team;
    }

    @Transactional
    public TeamDto createTeam(TeamDto teamDto) {
        boolean nameExists = teams.values().stream()
                .anyMatch(t -> t.getName().equalsIgnoreCase(teamDto.getName()));
        if (nameExists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Team with this name already exists");
        }
        Long id = nextId.getAndIncrement();
        TeamDto newTeam = new TeamDto(id, teamDto.getName());
        teams.put(id, newTeam);
        teamMembers.put(id, ConcurrentHashMap.newKeySet());
        return newTeam;
    }

    @Transactional
    public TeamDto updateTeam(Long id, TeamDto teamDto) {
        TeamDto existing = teams.get(id);
        if (existing == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found");
        }
        if (!existing.getName().equalsIgnoreCase(teamDto.getName())) {
            boolean nameExists = teams.values().stream()
                    .filter(t -> !t.getId().equals(id))
                    .anyMatch(t -> t.getName().equalsIgnoreCase(teamDto.getName()));
            if (nameExists) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Team with this name already exists");
            }
            existing.setName(teamDto.getName());
        }
        return existing;
    }

    @Transactional
    public void deleteTeam(Long id) {
        if (!teams.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found");
        }
        Set<Long> members = teamMembers.getOrDefault(id, Collections.emptySet());
        if (!members.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Team contains members");
        }
        teams.remove(id);
        teamMembers.remove(id);
    }

    public List<UserDto> getTeamMembers(Long teamId) {
        if (!teams.containsKey(teamId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found");
        }
        Set<Long> memberIds = teamMembers.getOrDefault(teamId, Collections.emptySet());
        return memberIds.stream()
                .map(userService::getUserById)
                .collect(Collectors.toList());
    }


    @Transactional
    public void removeAllMembersFromTeam(Long teamId) {
        if (!teams.containsKey(teamId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found");
        }
        Set<Long> members = teamMembers.get(teamId);
        if (members != null) {
            members.clear();
        }
    }

    @Transactional
    public void removeMemberFromTeam (Long teamId, Long userId){
        if (!teams.containsKey(teamId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found");
        }
        Set<Long> members = teamMembers.get(teamId);
        if (members == null || !members.contains(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not a member of this team");
        }
        members.remove(userId);
    }

//    @Transactional
//    public void addMemberToTeam(Long teamId, Long userId) {
//        if (!teams.containsKey(teamId)) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found");
//        }
//        userService.getUserById(userId);
//        Set<Long> members = teamMembers.computeIfAbsent(teamId, k -> ConcurrentHashMap.newKeySet());
//        members.add(userId);
//    }
}