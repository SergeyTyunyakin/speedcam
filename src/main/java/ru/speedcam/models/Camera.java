package ru.speedcam.models;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "camera", schema = "public")
//@Inheritance(strategy = InheritanceType.JOINED)
public class Camera implements Persistable<Long> {

    @jakarta.persistence.Id
    @Id
    @Column(name = "camera_id")
    @CsvBindByName(column = "camera_id")
    @CsvBindByPosition(position = 0)
    private Long id;

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

    public void setCameraModel(String camera_model) {
        this.cameraModel = camera_model;
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

    @CsvBindByName(column = "region_code")
    @CsvBindByPosition(position = 1)
    private String regionCode;
    @CsvBindByName(column = "src_camera")
    @CsvBindByPosition(position = 2)
    private String srcCamera;
    @CsvBindByName(column = "serial_no")
    @CsvBindByPosition(position = 3)
    private String serialNo;
    @CsvBindByName(column = "print_name")
    @CsvBindByPosition(position = 4)
    private String printName;
    @CsvBindByName(column = "camera_model")
    @CsvBindByPosition(position = 5)
    private String cameraModel;
    @CsvBindByName(column = "complex_id")
    @CsvBindByPosition(position = 6)
    private Long complexId;
    @CsvBindByName(column = "camera_place")
    @CsvBindByPosition(position = 7)
    private String cameraPlace;
    @Column(name = "gps_x")
    @CsvBindByName(column = "gps_x")
    @CsvBindByPosition(position = 8)
    private Double gpsX;
    @Column(name = "gps_y")
    @CsvBindByName(column = "gps_y")
    @CsvBindByPosition(position = 9)
    private Double gpsY;
    @CsvBindByName(column = "violname")
    @CsvBindByPosition(position = 10)
    private String violname;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "camera_state")
    private CameraState cameraState;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "edit_date")
    private LocalDate editDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Camera)) return false;

        Camera camera = (Camera) o;

        if (getId() != null ? !getId().equals(camera.getId()) : camera.getId() != null) return false;
        if (getRegionCode() != null ? !getRegionCode().equals(camera.getRegionCode()) : camera.getRegionCode() != null)
            return false;
        if (getSrcCamera() != null ? !getSrcCamera().equals(camera.getSrcCamera()) : camera.getSrcCamera() != null)
            return false;
        if (getSerialNo() != null ? !getSerialNo().equals(camera.getSerialNo()) : camera.getSerialNo() != null)
            return false;
        if (getPrintName() != null ? !getPrintName().equals(camera.getPrintName()) : camera.getPrintName() != null)
            return false;
        if (getCameraModel() != null ? !getCameraModel().equals(camera.getCameraModel()) : camera.getCameraModel() != null)
            return false;
        if (getComplexId() != null ? !getComplexId().equals(camera.getComplexId()) : camera.getComplexId() != null)
            return false;
        if (getCameraPlace() != null ? !getCameraPlace().equals(camera.getCameraPlace()) : camera.getCameraPlace() != null)
            return false;
        if (getGpsX() != null ? !getGpsX().equals(camera.getGpsX()) : camera.getGpsX() != null) return false;
        if (getGpsY() != null ? !getGpsY().equals(camera.getGpsY()) : camera.getGpsY() != null) return false;
        if (getViolname() != null ? !getViolname().equals(camera.getViolname()) : camera.getViolname() != null)
            return false;
        if (getCameraState() != camera.getCameraState()) return false;
        if (getCreateDate() != null ? !getCreateDate().equals(camera.getCreateDate()) : camera.getCreateDate() != null)
            return false;
        return getEditDate() != null ? getEditDate().equals(camera.getEditDate()) : camera.getEditDate() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getRegionCode() != null ? getRegionCode().hashCode() : 0);
        result = 31 * result + (getSrcCamera() != null ? getSrcCamera().hashCode() : 0);
        result = 31 * result + (getSerialNo() != null ? getSerialNo().hashCode() : 0);
        result = 31 * result + (getPrintName() != null ? getPrintName().hashCode() : 0);
        result = 31 * result + (getCameraModel() != null ? getCameraModel().hashCode() : 0);
        result = 31 * result + (getComplexId() != null ? getComplexId().hashCode() : 0);
        result = 31 * result + (getCameraPlace() != null ? getCameraPlace().hashCode() : 0);
        result = 31 * result + (getGpsX() != null ? getGpsX().hashCode() : 0);
        result = 31 * result + (getGpsY() != null ? getGpsY().hashCode() : 0);
        result = 31 * result + (getViolname() != null ? getViolname().hashCode() : 0);
        result = 31 * result + (getCameraState() != null ? getCameraState().hashCode() : 0);
        result = 31 * result + (getCreateDate() != null ? getCreateDate().hashCode() : 0);
        result = 31 * result + (getEditDate() != null ? getEditDate().hashCode() : 0);
        return result;
    }

    @Override
    public boolean isNew() {
        return (cameraState == CameraState.NEW);
    }

    public Long getCameraId() {
        return id;
    }

    public void setCameraId(Long id) {
        this.id = id;
    }
}
