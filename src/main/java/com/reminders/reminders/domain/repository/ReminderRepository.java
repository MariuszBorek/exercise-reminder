package com.reminders.reminders.domain.repository;

import com.reminders.reminders.domain.model.Reminder;
import com.reminders.reminders.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, Integer> {
    public List<Reminder> findAllByUser(User user);
    List<Reminder> findAllByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);
}
