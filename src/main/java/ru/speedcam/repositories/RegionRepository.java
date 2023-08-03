package ru.speedcam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.speedcam.models.Region;

public interface RegionRepository extends JpaRepository<Region, Long> {
}
