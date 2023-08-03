package ru.speedcam.controllers;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.speedcam.models.Camera;
import ru.speedcam.repositories.CameraRepository;
import ru.speedcam.repositories.UserRepository;
import ru.speedcam.security.details.UserDetailsImpl;
import ru.speedcam.transfer.UserDto;

import java.time.LocalDate;
import java.util.List;

import static ru.speedcam.transfer.UserDto.from;

@Controller
public class ViewController {

    private LocalDate dateFrom;
    private LocalDate dateTo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CameraRepository cameraRepository;

    @GetMapping("/view")
    public String getViewPage(@NotNull ModelMap model, Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }
        UserDetailsImpl details = (UserDetailsImpl) authentication.getPrincipal();
        UserDto user = from(details.getUser());
        model.addAttribute("user", user);
        if (dateFrom == null) {
            dateFrom = LocalDate.now();
        }
        if (dateTo == null) {
            dateTo = LocalDate.now();
        }
        if (dateFrom != null & dateTo != null) {
//            List<Camera> cameraList = cameraRepository.findAllByEditDateBetween(dateFrom, dateTo);
            List<Camera> cameraList = cameraRepository.findAllByCreateDateBetweenAndRegionCode(dateFrom, dateTo, "1114");
            cameraList.addAll(cameraRepository.findAllByEditDateBetweenAndRegionCode(dateFrom, dateTo, "1114"));
            model.addAttribute("cameraList", cameraList);
            model.addAttribute("dateFrom", dateFrom);
            model.addAttribute("dateTo", dateTo);
        }
        return "view.html";
    }

    @PostMapping("/view")
    public String setViewParams(@RequestParam("dateFrom") LocalDate dateFrom, @RequestParam("dateTo") LocalDate dateTo) {
        this.dateTo = dateTo;
        this.dateFrom = dateFrom;
        return "redirect:/view";
    }

}
