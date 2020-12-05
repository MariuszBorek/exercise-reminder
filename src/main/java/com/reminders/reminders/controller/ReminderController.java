package com.reminders.reminders.controller;

import com.reminders.reminders.domain.model.Reminder;
import com.reminders.reminders.model.ReminderForm;
import com.reminders.reminders.service.ReminderService;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/reminder")
public class ReminderController {

    private final ReminderService reminderService;

    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @GetMapping("/add")
    public String addReminder(Model model){
        model.addAttribute("reminderForm", new ReminderForm());
        return "reminder/add-form";
    }

    @PostMapping("/add")
    public String addReminderProcess(@ModelAttribute("reminderForm") ReminderForm reminderForm, Model model){
        reminderService.addReminder(reminderForm);
        return "redirect:/reminder/list";
    }

    @GetMapping("/list")
    public String getReminders(Model model){
        model.addAttribute("reminderList", reminderService.getAllRemindersForLoggedInUser());
        return "reminder/list";
    }

    @GetMapping("/list/week")
    public String getRemindersForWeek(Model model){
        final Map<LocalDate, List<Reminder>> remindersForDates = reminderService.getRemindersForDates(LocalDate.now(), LocalDate.now().plusWeeks(1));
        List<LocalDate> sortedDates = new ArrayList<>(remindersForDates.keySet());
        Collections.sort(sortedDates);
        model.addAttribute("remindersByDate", remindersForDates);
        model.addAttribute("dates", sortedDates);
        return "reminder/list-by-dates";
    }
}
