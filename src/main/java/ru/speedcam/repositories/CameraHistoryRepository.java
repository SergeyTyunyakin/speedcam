package ru.speedcam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.speedcam.models.CameraHistory;

import java.util.List;

public interface CameraHistoryRepository extends JpaRepository<CameraHistory, Long> {
    List<CameraHistory> findAllByCameraIdOrderByEditDateDesc(Long cameraId);
    List<CameraHistory> findAllByCameraIdOrderByEditDateAsc(Long cameraId);
}