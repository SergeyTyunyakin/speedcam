package ru.speedcam.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.speedcam.security.config.AppConfig;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class UploadProgressRunnable implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(UploadProgressRunnable.class);

    public static String BROADCAST_WS_ENDPOINT = "/ws-broadcast/update-progress";
    public static String BROADCAST_WS_PROGRESS_UPDATE_WITH_DETAILS = "/ws-broadcast/update-progress-with-details";

    ObjectMapper objectMapper;
    AppConfig appConfig;

    BroadcastWebsocketService broadcastWebsocketService;

    public UploadProgressRunnable(BroadcastWebsocketService broadcastWebsocketService, ObjectMapper objectMapper, AppConfig appConfig) {
        try {
            this.broadcastWebsocketService = broadcastWebsocketService;
            this.objectMapper = objectMapper;
            this.appConfig = appConfig;
        } catch (Exception exception) {
            logger.error(exception.getMessage());
        }
    }

    @Override
    public void run() {
//        for (int i = 0; i <= 100; ++i) {
//            try {
//                this.broadcastWebsocketService.broadcastProgressUpdate(BROADCAST_WS_ENDPOINT, i);
//                if (i >= 100) {
//                    Thread.currentThread().interrupt();
//                    break;
//                }
//                Thread.sleep(100);
//            } catch (Exception exception) {
//                logger.error(exception.getMessage());
//            }
//        }
//    }
        Map<String, Object> progress = new HashMap<>();
        progress.put("width", 0);
        progress.put("header", "Старт загрузки");
        try {
            this.broadcastWebsocketService.broadcastProgressUpdate(BROADCAST_WS_ENDPOINT, this.objectMapper.writeValueAsString(progress));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        OutputStream out = null;
        URLConnection conn = null;
        InputStream in = null;

        try {
            URL url = new URL(appConfig.getGibddUrl());
//            out = new BufferedOutputStream(new FileOutputStream(TEMP_CSV_NAME));
            out = new FileOutputStream(appConfig.getTmpFile());
            conn = url.openConnection();
            in = conn.getInputStream();
            byte[] buffer = new byte[1024];

            int numRead;
            long numWritten = 0;
            int percent = 0;
            int percentNew = 0;
            long fileSize = conn.getContentLength();

            while ((numRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, numRead);
                numWritten += numRead;
                percentNew = (int) ((numWritten * 100) / fileSize);
                if (percentNew > percent) {
                    percent = percentNew;
//                    this.broadcastWebsocketService.broadcastProgressUpdate(BROADCAST_WS_ENDPOINT, percent);
                    progress.clear();
                    progress.put("width", percentNew);
                    progress.put("header", "Загружено " + numWritten + " байт из " + fileSize);
                    this.broadcastWebsocketService.broadcastProgressUpdate(BROADCAST_WS_ENDPOINT, this.objectMapper.writeValueAsString(progress));
                }
            }
            Thread.currentThread().interrupt();

        } catch (Exception exception) {
            logger.error(exception.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException ioe) {
                logger.error(ioe.getMessage());
            }
        }

    }
}