package com.reminders.reminders.controller;

import com.reminders.reminders.exception.PasswordsDoNotMatchException;
import com.reminders.reminders.exception.UserArleadyExistsException;
import com.reminders.reminders.model.UserRegisterForm;
import com.reminders.reminders.service.UserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("userForm", new UserRegisterForm());
        return "user/register-form";
    }

    @PostMapping("/register")
    public String registerFormProcess(@ModelAttribute("userForm") final UserRegisterForm userForm, Model model) {
        try {
            userService.createUser(userForm);
        } catch( PasswordsDoNotMatchException e) {
            model.addAttribute("userForm",userForm);
            model.addAttribute("passwordError", true);
            return "user/register-form";
        } catch( UserArleadyExistsException e) {
            model.addAttribute("userForm",userForm);
            model.addAttribute("emailError", true);
            return "user/register-form";
        }
        return "redirect:/user/details";
    }

    @GetMapping("/details")
    public String details(Model model){
        model.addAttribute("loggedInUsername", userService.getLoggedInUsername().orElseThrow());
        return "user/details";
    }
}
