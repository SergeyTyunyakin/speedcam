package ru.speedcam.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {
    @Value("${csv.url}")
    private String gibddUrl;
    @Value("${csv.tmpFile}")
    private String tmpFile;

    public String getTmpFile() {
        return tmpFile;
    }

    public void setTmpFile(String tmpFile) {
        this.tmpFile = tmpFile;
    }

    public String getGibddUrl() {
        return gibddUrl;
    }

    public void setGibddUrl(String gibddUrl) {
        this.gibddUrl = gibddUrl;
    }
}