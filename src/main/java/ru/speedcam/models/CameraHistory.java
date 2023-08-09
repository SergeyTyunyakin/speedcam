package ru.speedcam.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "camera_history", schema = "public")
public class CameraHistory {
    @Id
    @GeneratedValue
    @Column(name = "history_id")
    private Long historyId;

    @Column(name = "camera_id")
    private Long cameraId;

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getSrcCamera() {
        return srcCamera;
    }

    public void setSrcCamera(String srcCamera) {
        this.srcCamera = srcCamera;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getPrintName() {
        return printName;
    }

    public void setPrintName(String printName) {
        this.printName = printName;
    }

    public String getCameraModel() {
        return cameraModel;
    }

    public void setCameraModel(String cameraModel) {
        this.cameraModel = cameraModel;
    }

    public Long getComplexId() {
        return complexId;
    }

    public void setComplexId(Long complexId) {
        this.complexId = complexId;
    }

    public String getCameraPlace() {
        return cameraPlace;
    }

    public void setCameraPlace(String cameraPlace) {
        this.cameraPlace = cameraPlace;
    }

    public Double getGpsX() {
        return gpsX;
    }

    public void setGpsX(Double gpsX) {
        this.gpsX = gpsX;
    }

    public Double getGpsY() {
        return gpsY;
    }

    public void setGpsY(Double gpsY) {
        this.gpsY = gpsY;
    }

    public String getViolname() {
        return violname;
    }

    public void setViolname(String violname) {
        this.violname = violname;
    }

    public CameraState getCameraState() {
        return cameraState;
    }

    public void setCameraState(CameraState cameraState) {
        this.cameraState = cameraState;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getEditDate() {
        return editDate;
    }

    public void setEditDate(LocalDate editDate) {
        this.editDate = editDate;
    }

    private String regionCode;

    private String srcCamera;

    private String serialNo;

    private String printName;

    private String cameraModel;

    private Long complexId;

    private String cameraPlace;
    @Column(name = "gps_x")
    private Double gpsX;

    @Column(name = "gps_y")
    private Double gpsY;

    private String violname;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "camera_state")
    private CameraState cameraState;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "edit_date")
    private LocalDate editDate;

    public CameraHistory(Long historyId, Camera camera) {
        this.historyId = historyId;
        this.cameraId = camera.getId();
        this.regionCode = camera.getRegionCode();
        this.srcCamera = camera.getSrcCamera();
        this.serialNo = camera.getSerialNo();
        this.printName = camera.getPrintName();
        this.cameraModel = camera.getCameraModel();
        this.complexId = camera.getComplexId();
        this.cameraPlace = camera.getCameraPlace();
        this.gpsX = camera.getGpsX();
        this.gpsY = camera.getGpsY();
        this.violname = camera.getViolname();
        this.cameraState = camera.getCameraState();
        this.createDate = camera.getCreateDate();
        this.editDate = camera.getEditDate();
    }
}
