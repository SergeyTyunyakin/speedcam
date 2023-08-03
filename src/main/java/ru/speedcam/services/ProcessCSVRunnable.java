package ru.speedcam.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.speedcam.models.Camera;
import ru.speedcam.models.CameraState;
import ru.speedcam.readers.CSVLineReader;
import ru.speedcam.readers.CSVLineReaderBuilder;
import ru.speedcam.security.config.AppConfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;

public class ProcessCSVRunnable implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ProcessCSVRunnable.class);
    public static String BROADCAST_WS_PROGRESS_UPDATE_WITH_DETAILS = "/ws-broadcast/update-progress-with-details";
    private static final int MAX_LIST_SIZE = 10;

    BroadcastWebsocketService broadcastWebsocketService;
    ObjectMapper objectMapper;
    AppConfig appConfig;

    public ProcessCSVRunnable(BroadcastWebsocketService broadcastWebsocketService, ObjectMapper objectMapper, AppConfig appConfig) {
        try {
            this.broadcastWebsocketService = broadcastWebsocketService;
            this.objectMapper = objectMapper;
            this.appConfig = appConfig;
//            this.messageDigest = MessageDigest.getInstance("MD5");
        } catch (Exception exception) {
            logger.error(exception.getMessage());
        }
    }

    @Override
    public void run() {

        HashMap<String, Object> payload = new HashMap<>();
//        LinkedList<String, Object> payload = new LinkedList<>();
        Path path = Paths.get(appConfig.getTmpFile());

        Reader reader = null;
        try {

            reader = new BufferedReader(new InputStreamReader(Files.newInputStream(path), "UTF-8"));


            int maxLineLength = 0;

            HashMap<Long, Camera> scMap = new HashMap<Long, Camera>();

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
            long size = 0;
            long percentNew = 0;
            long percent = 0;

            size = Files.size(path);
            String csvLine = csvReader.getNextLine();
            if (csvLine != null) {
                i = getLineLength(csvLine);
            }
            while ((csvLine = csvReader.getNextLine()) != null) {
                if (csvLine != null) {
                    nextLine = csvParser.parseLineMulti(csvLine);
                    Camera camera = new Camera(Long.parseLong(nextLine[0]), nextLine[1], nextLine[2], nextLine[3], nextLine[4], nextLine[5], Long.parseLong(nextLine[6]), nextLine[7], Double.parseDouble(nextLine[8]), Double.parseDouble(nextLine[9]), nextLine[10], CameraState.NEW, LocalDate.now(), null);
                    scMap.put(camera.getId(), camera);
                    i += getLineLength(csvLine);
                    percentNew = (int) ((i * 100) / size);
                    if (percentNew > percent) {
                        payload.put("place", camera.getCameraPlace());
                        payload.put("model", camera.getCameraModel());
                        payload.put("current", i);
                        payload.put("max", size);
                        this.broadcastWebsocketService.broadcastProgressUpdate(BROADCAST_WS_PROGRESS_UPDATE_WITH_DETAILS, this.objectMapper.writeValueAsString(payload));
                        payload.clear();
                        percent = percentNew;
                    }
                }
            }
            reader.close();
            csvReader.close();
            Thread.currentThread().interrupt();
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
//            for (int i = 0; i < words.size(); ++i) {
//                try {
//
//                    payload.put("word", words.get(i));
//                    payload.put("md5", this.messageDigest.digest(words.get(i).getBytes()));
//                    payload.put("current", i);
//                    payload.put("max", words.size());
//
//                    this.broadcastWebsocketService.broadcastProgressUpdate(BROADCAST_WS_PROGRESS_UPDATE_WITH_DETAILS, this.objectMapper.writeValueAsString(payload));
//                    if (i >= dictionary.length) {
//                        Thread.currentThread().interrupt();
//                        break;
//                    }
//                    Thread.sleep(100);
//                } catch (Exception exception) {
//                    logger.error(exception.getMessage());
//                }
//
//        }
}
