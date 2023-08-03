package ru.speedcam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.speedcam.models.Camera;

import java.time.LocalDate;
import java.util.List;

public interface CameraRepository extends JpaRepository<Camera, Long> {
    List<Camera> findAllByRegionCode(String regionCode);

    List<Camera> findAllByEditDateBetween(LocalDate dateFrom, LocalDate dateTo);

    List<Camera> findAllByEditDateBetweenAndRegionCode(LocalDate dateFrom, LocalDate dateTo, String regionCode);


    List<Camera> findAllByCreateDateBetween(LocalDate dateFrom, LocalDate dateTo);

    List<Camera> findAllByCreateDateBetweenAndRegionCode(LocalDate dateFrom, LocalDate dateTo, String regionCode);


//    Optional<User> findOneByLogin(String login);
}