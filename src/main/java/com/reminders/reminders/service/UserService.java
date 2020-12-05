package com.reminders.reminders.service;

import com.reminders.reminders.domain.model.User;
import com.reminders.reminders.domain.repository.UserReposiotry;
import com.reminders.reminders.exception.PasswordsDoNotMatchException;
import com.reminders.reminders.exception.UserArleadyExistsException;
import com.reminders.reminders.model.UserRegisterForm;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserReposiotry userReposiotry;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserReposiotry userReposiotry, PasswordEncoder passwordEncoder) {
        this.userReposiotry = userReposiotry;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userReposiotry.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("bad credentials"));
    }

    public void createUser(UserRegisterForm userForm) throws PasswordsDoNotMatchException, UserArleadyExistsException {
        if(!userForm.getPassword().equals(userForm.getPasswordRepeated())){
            throw new PasswordsDoNotMatchException();
        }
        if(userReposiotry.existsUserByEmail(userForm.getEmail())){
           throw new UserArleadyExistsException();
        }
        User user = new User();
        user.setEmail(userForm.getEmail());
        user.setFirstName(userForm.getFirstName());
        user.setLastName(userForm.getLastName());
        user.setPassword(passwordEncoder.encode(userForm.getPassword()));
        userReposiotry.save(user);
    }

    public Optional<String> getLoggedInUsername(){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return Optional.of(currentUserName);
        }
        return Optional.empty();
    }

    public Optional<User> getLoggedInUser(){
       return getLoggedInUsername().map(userReposiotry::findByEmail).map(Optional::get);
    }
}
