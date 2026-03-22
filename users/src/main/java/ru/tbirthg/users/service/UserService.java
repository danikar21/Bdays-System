package ru.tbirthg.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tbirthg.common.enums.RoleType;
import ru.tbirthg.users.repository.UserRepository;
import ru.tbirthg.users.dto.UserDto;
import ru.tbirthg.users.entity.UserEntity;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserDto getCurrentUser(String email) {
        UserEntity entity = userRepository.findByEmail(email).orElseGet(() -> createStubUser(email));
        return mapToDto(entity);
    }

    public UserDto getUserById(Long id) {
        UserEntity entity = userRepository.findById(id).orElseGet(() -> createStubUser("user" + id + "@example.com"));
        return mapToDto(entity);
    }

    public List<UserDto> getAllUsers(String sortBy, String direction) {
        List<UserEntity> users = userRepository.findAll();
        if (users.isEmpty()) {
            return List.of(
                    mapToDto(createStubUser("admin@example.com")),
                    mapToDto(createStubUser("test@example.com"))
            );
        }
        return users.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Transactional
    public UserDto updateCurrentUser(String email, UserDto updateDto) {
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setEmail(email);
        dto.setFirstName(updateDto.getFirstName());
        dto.setPatronymic(updateDto.getPatronymic());
        dto.setLastName(updateDto.getLastName());
        dto.setBirthDate(updateDto.getBirthDate());
        dto.setPosition(updateDto.getPosition());
        dto.setRole(RoleType.USER);
        return dto;
    }

    @Transactional
    public UserDto createUser(UserDto createDto) {
        UserDto dto = new UserDto();
        dto.setId(99L);
        dto.setEmail(createDto.getEmail());
        dto.setFirstName(createDto.getFirstName());
        dto.setPatronymic(createDto.getPatronymic());
        dto.setLastName(createDto.getLastName());
        dto.setBirthDate(createDto.getBirthDate());
        dto.setPosition(createDto.getPosition());
        dto.setRole(RoleType.USER);
        return dto;
    }

    @Transactional
    public UserDto updateUser(Long id, UserDto updateDto) {
        UserDto dto = new UserDto();
        dto.setId(id);
        dto.setEmail(updateDto.getEmail());
        dto.setFirstName(updateDto.getFirstName());
        dto.setPatronymic(updateDto.getPatronymic());
        dto.setLastName(updateDto.getLastName());
        dto.setBirthDate(updateDto.getBirthDate());
        dto.setPosition(updateDto.getPosition());
        dto.setRole(RoleType.USER);
        return dto;
    }

    private UserEntity createStubUser(String email) {
        UserEntity stub = new UserEntity();
        stub.setId(1L);
        stub.setEmail(email);
        stub.setFirstName("Stub");
        stub.setPatronymic("Stubovich");
        stub.setLastName("User");
        stub.setBirthDate(LocalDate.of(1990, 1, 1));
        stub.setPosition("Developer");
        stub.setRole(RoleType.GUEST);
        return stub;
    }

    private UserDto mapToDto(UserEntity entity) {
        UserDto dto = new UserDto();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setFirstName(entity.getFirstName());
        dto.setPatronymic(entity.getPatronymic());
        dto.setLastName(entity.getLastName());
        dto.setBirthDate(entity.getBirthDate());
        dto.setPosition(entity.getPosition());
        dto.setRole(entity.getRole());
        return dto;
    }
}