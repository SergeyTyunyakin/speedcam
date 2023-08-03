package ru.speedcam.readers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import ru.speedcam.models.Camera;
import ru.speedcam.models.CameraHistory;
import ru.speedcam.models.CameraState;
import ru.speedcam.repositories.CameraHistoryRepository;
import ru.speedcam.repositories.CameraRepository;
import ru.speedcam.security.config.AppConfig;
import ru.speedcam.services.BroadcastWebsocketService;
import ru.speedcam.services.CameraServiceImpl;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CSVUploader {

    @Autowired
    private CameraServiceImpl cameraService;

    private static final Logger logger = LoggerFactory.getLogger(CSVUploader.class);
    public static String BROADCAST_WS_PROGRESS_UPDATE_WITH_DETAILS = "/ws-broadcast/update-progress-with-details";
    private static final int MAX_LIST_SIZE = 10;

    private BroadcastWebsocketService broadcastWebsocketService;
    private ObjectMapper objectMapper;
    private AppConfig appConfig;

    public CSVUploader(BroadcastWebsocketService broadcastWebsocketService, ObjectMapper objectMapper, AppConfig appConfig) {
        try {
            this.broadcastWebsocketService = broadcastWebsocketService;
            this.objectMapper = objectMapper;
            this.appConfig = appConfig;
//            this.messageDigest = MessageDigest.getInstance("MD5");
        } catch (Exception exception) {
            logger.error(exception.getMessage());
        }
    }

    public int getCSVLineLength(String[] lines) {
        if (lines != null) {
            return Arrays.toString(lines).getBytes().length + 2;
        } else {
            return 0;
        }
    }

    public int getLineLength(String line) {
        if (line != null) {
            return line.getBytes().length + 2;
        } else {
            return 0;
        }
    }

    public void uploadCSV(MultipartFile file, LocalDate loadDate, CameraRepository cameraRepository, CameraHistoryRepository cameraHistoryRepository) {
        HashMap<String, Object> payload = new HashMap<>();
        Reader reader = null;
        long size = 0;
//        LinkedList<String, Object> payload = new LinkedList<>();
        try {
            payload.put("place", "");
            payload.put("model", "");
            payload.put("current", 0);
            payload.put("max", 100);
            payload.put("detailsVisibility", false);
            this.broadcastWebsocketService.broadcastProgressUpdate(BROADCAST_WS_PROGRESS_UPDATE_WITH_DETAILS, this.objectMapper.writeValueAsString(payload));
            Thread.sleep(100);

            if (file.isEmpty()) {

                OutputStream out = null;
                URLConnection conn = null;
                InputStream in = null;

                payload.put("header", "Начало загрузки с сервера");
                this.broadcastWebsocketService.broadcastProgressUpdate(BROADCAST_WS_PROGRESS_UPDATE_WITH_DETAILS, this.objectMapper.writeValueAsString(payload));
                try {
                    URL url = new URL(appConfig.getGibddUrl());
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
                            payload.put("header", "Загружено " + numWritten + " байт из " + fileSize);
                            payload.put("current", percentNew);
                            payload.put("max", 100);
                            this.broadcastWebsocketService.broadcastProgressUpdate(BROADCAST_WS_PROGRESS_UPDATE_WITH_DETAILS, this.objectMapper.writeValueAsString(payload));
                        }
                    }
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

                Path path = Paths.get(appConfig.getTmpFile());
                size = Files.size(path);
                reader = new BufferedReader(new InputStreamReader(Files.newInputStream(path), "UTF-8"));


            } else {
                reader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
                size = file.getSize();
            }

            int maxLineLength = 0;

            HashMap<Long, Camera> newSpeedCams = new HashMap<Long, Camera>();

            CSVParser csvParser = new CSVParserBuilder()
                    .withSeparator(';')
                    .withIgnoreQuotations(false)
                    .build();

            CSVLineReader csvReader = new CSVLineReaderBuilder(reader)
//                    .withSkipLines(1)
                    .withCSVParser(csvParser)
                    .build();

            String[] nextLine;
            long i = 0;
            long percentNew = 0;
            long percent = 0;

            String csvLine = csvReader.getNextLine();
            if (csvLine != null) {
                i = getLineLength(csvLine);
            }
            payload.put("current", 0);
            payload.put("max", size);
            payload.put("header", "Обработка CSV");
            this.broadcastWebsocketService.broadcastProgressUpdate(BROADCAST_WS_PROGRESS_UPDATE_WITH_DETAILS, this.objectMapper.writeValueAsString(payload));
            Thread.sleep(500);
            while ((csvLine = csvReader.getNextLine()) != null) {
                if (csvLine != null) {
                    nextLine = csvParser.parseLineMulti(csvLine);
                    Camera camera = new Camera(Long.parseLong(nextLine[0]), nextLine[1], nextLine[2], nextLine[3], nextLine[4], nextLine[5], Long.parseLong(nextLine[6]), nextLine[7], Double.parseDouble(nextLine[8]), Double.parseDouble(nextLine[9]), nextLine[10], CameraState.NEW, loadDate, null);
                    newSpeedCams.put(camera.getId(), camera);
                    i += getLineLength(csvLine);
                    percentNew = (int) ((i * 100) / size);
                    if (percentNew > percent) {
                        payload.put("place", camera.getCameraPlace());
                        payload.put("model", camera.getCameraModel());
                        payload.put("current", i);
                        payload.put("detailsVisibility", true);
                        this.broadcastWebsocketService.broadcastProgressUpdate(BROADCAST_WS_PROGRESS_UPDATE_WITH_DETAILS, this.objectMapper.writeValueAsString(payload));
                        percent = percentNew;
                    }
                }
            }
            reader.close();
            csvReader.close();

            if (newSpeedCams.size() > 0) {

                cameraService = new CameraServiceImpl();
                List<Camera> oldSpeedCams = cameraRepository.findAll();
                List<CameraHistory> cameraHistoryList = new ArrayList<>();
                List<Camera> delSpeedCams = new ArrayList<>();

                i = 0;
                percentNew = 0;
                percent = 0;
                size = oldSpeedCams.size();
                payload.put("current", 0);
                payload.put("max", size);
                payload.put("header", "Сравнение данных");
                payload.put("detailsVisibility", false);
                this.broadcastWebsocketService.broadcastProgressUpdate(BROADCAST_WS_PROGRESS_UPDATE_WITH_DETAILS, this.objectMapper.writeValueAsString(payload));
                Thread.sleep(500);
                cameraRepository.flush();

                for (Camera oldCamera : oldSpeedCams) {
                    Camera newCamera = newSpeedCams.get(oldCamera.getId());
                    if (newCamera != null) {
                        newCamera.setCreateDate(oldCamera.getCreateDate());
                        newCamera.setEditDate(oldCamera.getEditDate());
                        if (oldCamera.getEditDate() != null) {
                            if (!oldCamera.getCameraState().equals(CameraState.DELETED)) {
                                newCamera.setCameraState(oldCamera.getCameraState());
                            }
                        }
                        if (newCamera.equals(oldCamera)) {
                            newSpeedCams.remove(oldCamera.getId());
                        } else {
                            newCamera.setEditDate(loadDate);
                            if (oldCamera.getCameraState() == CameraState.DELETED) {
                                newCamera.setCameraState(CameraState.RESTORED);
                            } else {
                                newCamera.setCameraState(CameraState.EDITED);
                            }
                            newSpeedCams.replace(newCamera.getId(), newCamera);
                            cameraHistoryList.add(new CameraHistory(null, oldCamera));
                        }
                    } else {
                        oldCamera.setCameraState(CameraState.DELETED);
                        oldCamera.setEditDate(loadDate);
                        delSpeedCams.add(oldCamera);
                    }
                    i++;
                    percentNew = (int) ((i * 100) / size);
                    if (percentNew > percent) {
                        payload.put("current", percentNew);
                        payload.put("max", 100);
                        this.broadcastWebsocketService.broadcastProgressUpdate(BROADCAST_WS_PROGRESS_UPDATE_WITH_DETAILS, this.objectMapper.writeValueAsString(payload));
                    }
                }
                i = 0;
                size = newSpeedCams.values().size();
                percentNew = 0;
                percent = 0;
                payload.put("current", 0);
                payload.put("header", "Сохранение данных");
                payload.put("max", size);
                this.broadcastWebsocketService.broadcastProgressUpdate(BROADCAST_WS_PROGRESS_UPDATE_WITH_DETAILS, this.objectMapper.writeValueAsString(payload));
//                for (Camera camera : newSpeedCams.values()) {
//                    cameraRepository.save(camera);
//                    i++;
//                    percentNew = (int) ((i * 100) / size);
//                    if (percentNew > percent) {
//                        percent = percentNew;
//                        payload.put("header", "Сохранено " + i + " записей из " + size);
//                        payload.put("current", percentNew);
//                        payload.put("max", 100);
//                        this.broadcastWebsocketService.broadcastProgressUpdate(BROADCAST_WS_PROGRESS_UPDATE_WITH_DETAILS, this.objectMapper.writeValueAsString(payload));
//                        cameraRepository.flush();
//                    }
//                }
                cameraRepository.saveAll(newSpeedCams.values());
                cameraRepository.saveAll(delSpeedCams);
                cameraHistoryRepository.saveAll(cameraHistoryList);
                payload.put("current", size);
                payload.put("header", "Данные сохранены");
                this.broadcastWebsocketService.broadcastProgressUpdate(BROADCAST_WS_PROGRESS_UPDATE_WITH_DETAILS, this.objectMapper.writeValueAsString(payload));

            }

        } catch (Exception exception) {
            logger.error(exception.getMessage());
        }


    }

}
