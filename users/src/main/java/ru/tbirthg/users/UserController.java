package ru.tbirthg.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.tbirthg.common.enums.RoleType;
import ru.tbirthg.users.dto.UserDto;
import ru.tbirthg.users.service.UserService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Пользователи")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @Operation(summary = "Получить профиль текущего пользователя")
    public UserDto getCurrentUser() {
        return new UserDto(1L, "admin@example.com", "Admin", "Adminov", "Adminov", LocalDate.of(1990, 1, 1), "Developer", RoleType.GUEST);
    }

    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Обновить профиль текущего пользователя (только для USER и ADMIN)")
    public ResponseEntity<UserDto> updateCurrentUser(@Valid @RequestBody UserDto updateDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        UserDto updated = userService.updateCurrentUser(email, updateDto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID (только для USER и ADMIN)")
    public UserDto getUserById(@PathVariable Long id) {
        return new UserDto(id, "user" + id + "@example.com", "Example", "Examplevich", "Examplov", LocalDate.of(2000, 1, 1), "Manager", RoleType.USER);
    }

    @GetMapping
    @Operation(summary = "Получить список всех пользователей (постранично) (только для USER и ADMIN)")
    public List<UserDto> getAllUsers(
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        return userService.getAllUsers(sortBy, direction);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Создать пользователя (только для ADMIN)")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto createDto) {
        UserDto created = userService.createUser(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Обновить пользователя (только для ADMIN)")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto updateDto) {
        UserDto updated = userService.updateUser(id, updateDto);
        return ResponseEntity.ok(updated);
    }
}