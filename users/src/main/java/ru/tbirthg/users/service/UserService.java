package ru.tbirthg.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.tbirthg.common.enums.RoleType;
import ru.tbirthg.users.repository.UserRepository;
import ru.tbirthg.users.dto.UserDto;
import ru.tbirthg.users.entity.UserEntity;


import java.time.LocalDate;
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

    private final Map<Long, UserDto> users = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    {
        createUser(new UserDto(null, "one@example.com", "One", "Onevich", "Adminov",
                LocalDate.of(2003, 1, 1), "Admin", RoleType.ADMIN));
        createUser(new UserDto(null, "two@example.com", "Two", "Twovich", "Userov",
                LocalDate.of(2001, 3, 17), "Developer", RoleType.USER));
        createUser(new UserDto(null, "three@example.com", "Three", "Threevich", "Guestov",
                LocalDate.of(2000, 10, 27), "Trainee", RoleType.GUEST));
    }

    public UserDto getCurrentUser(String email) {
        return users.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public UserDto getUserById(Long id) {
        UserDto user = users.get(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return user;
    }

    public List<UserDto> getAllUsers(String sortBy, String direction) {
        List<UserDto> list = new ArrayList<>(users.values());
        Comparator<UserDto> comparator = switch (sortBy.toLowerCase()) {
            case "firstname" -> Comparator.comparing(UserDto::getFirstName);
            case "lastname" -> Comparator.comparing(UserDto::getLastName);
            default -> Comparator.comparing(UserDto::getId);
        };
        if ("desc".equalsIgnoreCase(direction)) {
            comparator = comparator.reversed();
        }
        list.sort(comparator);
        return list;
    }

    @Transactional
    public UserDto createUser(UserDto createDto) {
        if (users.values().stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(createDto.getEmail()))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with this email already exists");
        }
        Long id = nextId.getAndIncrement();
        UserDto newUser = new UserDto(
                id,
                createDto.getEmail(),
                createDto.getFirstName(),
                createDto.getPatronymic(),
                createDto.getLastName(),
                createDto.getBirthDate(),
                createDto.getPosition(),
                createDto.getRole() != null ? createDto.getRole() : RoleType.USER
        );
        users.put(id, newUser);
        return newUser;
    }

    @Transactional
    public UserDto updateUser(Long id, UserDto updateDto) {
        UserDto existing = getUserById(id);
        updateUserFields(existing, updateDto);
        return existing;
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!users.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        users.remove(id);
    }

    private void updateUserFields(UserDto existing, UserDto updateDto) {
        if (updateDto.getEmail() != null && !updateDto.getEmail().equalsIgnoreCase(existing.getEmail())) {
            boolean emailExists = users.values().stream()
                    .anyMatch(u -> u.getEmail().equalsIgnoreCase(updateDto.getEmail()) && !u.getId().equals(existing.getId()));
            if (emailExists) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already used");
            }
            existing.setEmail(updateDto.getEmail());
        }

        if (updateDto.getFirstName() != null) existing.setFirstName(updateDto.getFirstName());
        if (updateDto.getPatronymic() != null) existing.setPatronymic(updateDto.getPatronymic());
        if (updateDto.getLastName() != null) existing.setLastName(updateDto.getLastName());
        if (updateDto.getBirthDate() != null) existing.setBirthDate(updateDto.getBirthDate());
        if (updateDto.getPosition() != null) existing.setPosition(updateDto.getPosition());
        if (updateDto.getRole() != null) existing.setRole(updateDto.getRole());
    }
}