package ru.tbirthg.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.tbirthg.common.enums.RoleType;
import ru.tbirthg.users.dto.UserCreateRequestDto;
import ru.tbirthg.users.dto.UserResponseDto;
import ru.tbirthg.users.dto.UserUpdateRequestDto;
import ru.tbirthg.users.entity.TeamEntity;
import ru.tbirthg.users.entity.UserEntity;
import ru.tbirthg.users.repository.TeamRepository;
import ru.tbirthg.users.repository.UserRepository;


import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto getCurrentUser(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return mapToDto(user);
    }

    public UserResponseDto getUserById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return mapToDto(user);
    }

    public List<UserResponseDto> getAllUsers(String sortBy, String direction) {
        Sort.Direction dir = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(dir, sortBy);
        List<UserEntity> users = userRepository.findAll(sort);
        return users.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Transactional
    public UserResponseDto createUser(UserCreateRequestDto userCreateRequestDto) {
        if (userRepository.findByEmail(userCreateRequestDto.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with this email already exists");
        }
        TeamEntity team = teamRepository.findById(userCreateRequestDto.getTeamId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found"));
        UserEntity user = new UserEntity();
        user.setEmail(userCreateRequestDto.getEmail());

        user.setPasswordHash(passwordEncoder.encode(userCreateRequestDto.getPassword()));
        user.setFirstName(userCreateRequestDto.getFirstName());
        user.setPatronymic(userCreateRequestDto.getPatronymic());
        user.setLastName(userCreateRequestDto.getLastName());
        user.setBirthDate(userCreateRequestDto.getBirthDate());
        user.setPosition(userCreateRequestDto.getPosition());
        user.setRole(RoleType.USER);
        user.setActive(true);
        user.setTeam(team);
        user.setCreatedAt(OffsetDateTime.now());
        UserEntity saved = userRepository.save(user);
        return mapToDto(saved);
    }

    @Transactional
    public UserResponseDto updateUser(Long id, UserUpdateRequestDto userUpdateRequestDto) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (userUpdateRequestDto.getEmail() != null && !userUpdateRequestDto.getEmail().isBlank()) {
            if (!userUpdateRequestDto.getEmail().equals(user.getEmail())) {
                if (userRepository.findByEmail(userUpdateRequestDto.getEmail()).isPresent()) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already used");
                }
                user.setEmail(userUpdateRequestDto.getEmail());
            }
        }

        if (userUpdateRequestDto.getLastName() != null) user.setLastName(userUpdateRequestDto.getLastName());
        if (userUpdateRequestDto.getFirstName() != null) user.setFirstName(userUpdateRequestDto.getFirstName());
        if (userUpdateRequestDto.getPatronymic() != null) user.setPatronymic(userUpdateRequestDto.getPatronymic());
        if (userUpdateRequestDto.getBirthDate() != null) user.setBirthDate(userUpdateRequestDto.getBirthDate());
        if (userUpdateRequestDto.getTeamId() != null) {
            TeamEntity team = teamRepository.findById(userUpdateRequestDto.getTeamId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found"));
            user.setTeam(team);
        }
        if (userUpdateRequestDto.getPosition() != null) user.setPosition(userUpdateRequestDto.getPosition());
        if (userUpdateRequestDto.getPassword() != null && !userUpdateRequestDto.getPassword().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(userUpdateRequestDto.getPassword()));
        }
        if (userUpdateRequestDto.getRole() != null) user.setRole(userUpdateRequestDto.getRole());

        UserEntity updated = userRepository.save(user);
        return mapToDto(updated);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userRepository.deleteById(id);
    }

    private UserResponseDto mapToDto(UserEntity entity) {
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