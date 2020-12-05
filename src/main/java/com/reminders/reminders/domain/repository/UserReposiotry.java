package com.reminders.reminders.domain.repository;

import com.reminders.reminders.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserReposiotry extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String email);

    boolean existsUserByEmail(String email);
}
