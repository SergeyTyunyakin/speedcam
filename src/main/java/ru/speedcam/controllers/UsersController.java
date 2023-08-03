package ru.speedcam.controllers;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import ru.speedcam.repositories.UserRepository;

@Controller
public class UsersController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public String getUsersPage(@NotNull ModelMap model) {
        model.addAttribute("usersFromServer", userRepository.findAll());
        return "users.html";
    }
}
