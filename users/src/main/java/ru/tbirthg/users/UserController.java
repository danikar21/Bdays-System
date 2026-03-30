package ru.tbirthg.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.tbirthg.users.dto.UserCreateRequestDto;
import ru.tbirthg.users.dto.UserResponseDto;
import ru.tbirthg.users.dto.UserUpdateRequestDto;
import ru.tbirthg.users.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Пользователи")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "Получить профиль текущего пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content)
    })
    @PreAuthorize("isAuthenticated()")
    public UserResponseDto getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userService.getCurrentUser(email);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID (только для USER и ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен (требуется роль USER или ADMIN)",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content)
    })
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public UserResponseDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/all")
    @Operation(summary = "Получить список всех пользователей (без сортировки), только для USER и ADMIN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponseDto.class)))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен (требуется роль USER или ADMIN)",
                    content = @Content)
    })
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<UserResponseDto> getAllUsersSimple() {
        return userService.getAllUsers("id", "asc");
    }

    @GetMapping
    @Operation(summary = "Получить список всех пользователей (с сортировкой), только для USER и ADMIN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponseDto.class)))),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен (требуется роль USER или ADMIN)",
                    content = @Content)
    })
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<UserResponseDto> getAllUsers(
            @Parameter(description = "Поле для сортировки (id, firstName, lastName)", example = "id")
            @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Направление сортировки (asc/desc)", example = "asc")
            @RequestParam(defaultValue = "asc") String direction) {
        return userService.getAllUsers(sortBy, direction);
    }

    @PostMapping
    @Operation(summary = "Создать пользователя (только для ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Успешный ответ",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав (требуется роль ADMIN)",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Email уже существует",
                    content = @Content)
    })
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto createUser(@Valid @RequestBody UserCreateRequestDto userCreateRequestDto) {
        return userService.createUser(userCreateRequestDto);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновить пользователя (только для ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав (требуется роль ADMIN)",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Email уже используется",
                    content = @Content)
    })
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        return userService.updateUser(id, userUpdateRequestDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя (только для ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Успешно удалено"),
            @ApiResponse(responseCode = "401", description = "Не авторизован",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав (требуется роль ADMIN)",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content)
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}