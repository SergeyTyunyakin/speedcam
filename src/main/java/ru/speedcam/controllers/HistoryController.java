package ru.speedcam.controllers;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.speedcam.models.CameraHistory;
import ru.speedcam.repositories.CameraHistoryRepository;
import ru.speedcam.repositories.CameraRepository;
import ru.speedcam.repositories.UserRepository;
import ru.speedcam.security.details.UserDetailsImpl;
import ru.speedcam.transfer.UserDto;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.speedcam.transfer.UserDto.from;

@Controller
public class HistoryController {

    private LocalDate dateFrom;
    private LocalDate dateTo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CameraRepository cameraRepository;

    @Autowired
    private CameraHistoryRepository cameraHistoryRepository;

    @GetMapping("/history")
    public String getViewPage(@NotNull ModelMap model, Authentication authentication, @RequestParam("cameraId") Long cameraId) {
        if (authentication == null) {
            return "redirect:/login";
        }
        UserDetailsImpl details = (UserDetailsImpl) authentication.getPrincipal();
        UserDto user = from(details.getUser());
        model.addAttribute("user", user);
        if (cameraId != null) {
            List<CameraHistory> cameraHistory = cameraHistoryRepository.findAllByCameraIdOrderByEditDateAsc(cameraId);
            CameraHistory currentData = new CameraHistory(null, cameraRepository.findCameraById(cameraId));
            cameraHistory.add(currentData);

            List<CameraHistory> sortedHistory =
                    cameraHistory
                            .stream()
                            .sorted(Comparator.comparing(CameraHistory::getEditDate, Comparator.nullsFirst(Comparator.naturalOrder())))
                            .collect(Collectors.toList());

            model.addAttribute("cameraHistory", sortedHistory);
            model.addAttribute("currentData", currentData);
        }
        return "history.html";
    }

}
