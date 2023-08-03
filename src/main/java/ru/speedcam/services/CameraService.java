package ru.speedcam.services;

import ru.speedcam.models.Camera;

import java.util.List;

public interface CameraService {

    List<Camera> findAll();

    void saveAll(List<Camera> cameras);

}
