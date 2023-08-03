package ru.speedcam.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.speedcam.forms.UserForm;
import ru.speedcam.services.SignUpService;

@Controller
public class SignupController {

    @Autowired
    private SignUpService service;

    @GetMapping("/signup")
    public String getSignupPage() {
        return "signup.html";
    }

    @PostMapping("/signup")
    public String signUp(UserForm userForm) {
        service.signUp(userForm);
        return "redirect:/login";
    }
}