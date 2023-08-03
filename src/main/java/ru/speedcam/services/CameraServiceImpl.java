package ru.speedcam.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.speedcam.models.Camera;
import ru.speedcam.repositories.CameraRepository;

import java.util.List;

@Service
public class CameraServiceImpl implements CameraService{

    @Autowired
    private CameraRepository cameraRepository;

    @Override
    public void saveAll(List<Camera> cameras) {
        cameraRepository.saveAll(cameras);
    }

    @Override
    public List<Camera> findAll() {
        return cameraRepository.findAll();
    }
}
