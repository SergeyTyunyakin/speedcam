package ru.speedcam.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import ru.speedcam.security.details.UserDetailsImpl;
import ru.speedcam.transfer.UserDto;

import static ru.speedcam.transfer.UserDto.from;

@Controller
public class ProfileController {
    @GetMapping("/profile")
    public String getProfilePage(ModelMap model, Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }
        UserDetailsImpl details = (UserDetailsImpl)authentication.getPrincipal();
        UserDto user = from(details.getUser());
        model.addAttribute("user", user);
        return "profile.html";

    }
}