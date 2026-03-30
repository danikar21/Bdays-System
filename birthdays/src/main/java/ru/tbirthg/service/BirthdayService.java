package ru.tbirthg.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tbirthg.dto.BirthdayResponseDto;
import ru.tbirthg.users.entity.UserEntity;
import ru.tbirthg.users.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BirthdayService {
    private final UserRepository userRepository;

    public List<BirthdayResponseDto> getUpcomingBirthdays() {
        List<UserEntity> users = userRepository.findUsersWithBirthdayInCurrentAndNextMonth();
        return users.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

//    public List<BirthdayResponseDto> getTodayBirthdays() {
//        List<UserEntity> users = userRepository.findTodayBirthdays();
//        return users.stream().
//                map(this::mapToDto).
//                collect(Collectors.toList());
//    }

    private BirthdayResponseDto mapToDto(UserEntity user) {
        return new BirthdayResponseDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getBirthDate()
        );
    }
}