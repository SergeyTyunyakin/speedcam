package ru.speedcam.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import ru.speedcam.readers.CSVUploader;
import ru.speedcam.repositories.CameraHistoryRepository;
import ru.speedcam.repositories.CameraRepository;
import ru.speedcam.security.config.AppConfig;
import ru.speedcam.services.BroadcastWebsocketService;
import ru.speedcam.services.UploadProgressRunnable;

import java.time.LocalDate;

@Controller
public class PageController {
    @Autowired
    BroadcastWebsocketService broadcastWebsocketService;

    @Autowired
    public AppConfig appConfig;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CameraRepository cameraRepository;

    @Autowired
    CameraHistoryRepository cameraHistoryRepository;

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

//    @RequestMapping(value = "upload", method = RequestMethod.POST)
//
    @RequestMapping(value = "upload", method = RequestMethod.POST, produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file, @RequestParam("loadDate") LocalDate loadDate) {
        new UploadProgressRunnable(broadcastWebsocketService, objectMapper, appConfig).run();
        return "";
    }

    @RequestMapping(value = "process-csv", method = RequestMethod.POST, produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public String processCSV(@RequestParam("file") MultipartFile file, @RequestParam("loadDate") LocalDate loadDate) {
        new CSVUploader(broadcastWebsocketService, objectMapper, appConfig).uploadCSV(file, loadDate, cameraRepository, cameraHistoryRepository);
        return "";
    }
}