package com.reminders.reminders.service;

import com.reminders.reminders.domain.model.Reminder;
import com.reminders.reminders.domain.repository.ReminderRepository;
import com.reminders.reminders.model.ReminderForm;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ReminderService {
    private final UserService userService;
    private final ReminderRepository reminderRepository;

    public ReminderService(UserService userService, ReminderRepository reminderRepository) {
        this.userService = userService;
        this.reminderRepository = reminderRepository;
    }

    public void addReminder(ReminderForm reminderForm) {
        Reminder reminder = new Reminder();
        reminder.setDate(reminderForm.getDate());
        reminder.setTime(reminderForm.getTime());
        reminder.setDescription(reminderForm.getDescription());
        reminder.setUser(userService.getLoggedInUser().orElseThrow());
        reminderRepository.save(reminder);
    }

    public List<Reminder> getAllRemindersForLoggedInUser() {
        return reminderRepository.findAllByUser(userService.getLoggedInUser().orElseThrow());
    }

    public Map<LocalDate, List<Reminder>> getRemindersForDates(LocalDate startDate, LocalDate endDate){
        List<Reminder> remindersForDates = reminderRepository.findAllByUserAndDateBetween(userService.getLoggedInUser().orElseThrow(),startDate,endDate);
        return remindersForDates.stream().collect(Collectors.groupingBy(Reminder::getDate));
    }
}
