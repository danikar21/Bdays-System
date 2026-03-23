package ru.tbirthg.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tbirthg.users.entity.TeamEntity;

@Repository
public interface TeamRepository extends JpaRepository<TeamEntity, Long> {
}