package ru.tbirthg.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.tbirthg.users.entity.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmailAndActiveTrue(String email);

    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findByTeamId(Long teamId);

    boolean existsByTeamId(Long teamId);

    @Query(value = """
    SELECT *
    FROM accounts
    WHERE birth_date IS NOT NULL
    AND (
    (EXTRACT(MONTH FROM birth_date) = EXTRACT(MONTH FROM CURRENT_DATE)
    AND EXTRACT(DAY FROM birth_date) >= EXTRACT(DAY FROM CURRENT_DATE))
    OR EXTRACT(MONTH FROM birth_date) = EXTRACT(MONTH FROM CURRENT_DATE + INTERVAL '1 month')
    )
    ORDER BY EXTRACT(MONTH FROM birth_date), EXTRACT(DAY FROM birth_date)
    """, nativeQuery = true)
    List<UserEntity> findUsersWithBirthdayInCurrentAndNextMonth();

//    @Query(value = """
//    SELECT *
//    FROM accounts
//    WHERE birth_date IS NOT NULL
//      AND EXTRACT(MONTH FROM birth_date) = EXTRACT(MONTH FROM CURRENT_DATE)
//      AND EXTRACT(DAY FROM birth_date) = EXTRACT(DAY FROM CURRENT_DATE)
//    """, nativeQuery = true)
//    List<UserEntity> findTodayBirthdays();
}