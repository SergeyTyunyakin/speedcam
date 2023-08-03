package ru.speedcam.models;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public class SampleViewModel {
    private String placeholder;
    private String loadDate;
    private MultipartFile file;

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getLoadDate() {
        return loadDate;
    }

    public LocalDate getLoadDateAsDate() {
        return LocalDate.parse(loadDate);
    }

    public void setLoadDate(String loadDate) {
        this.loadDate = loadDate;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}