package com.implemica.application.service.dto;

import com.implemica.application.domain.enumeration.BodyType;
import com.implemica.application.domain.enumeration.Brand;
import com.implemica.application.domain.enumeration.GearBoxType;

import javax.persistence.Column;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.implemica.application.domain.Car} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CarDTO implements Serializable {

    private Long id;

    @NotNull
    private Brand carBrand;

    @NotBlank(message = "Model is required.")
    @Size(min = 2, max = 40)
    private String carModel;

    private BodyType carBodyType;

    private String carImageUrl;

    @NotNull
    @Min(value = 1900)
    @Max(value = 2100)
    private Integer carYear;

    @DecimalMin(value = "0")
    @DecimalMax(value = "30.0")
    private Float carEngineVolume;

    private GearBoxType carGearboxType;

    @Size(min = 20, max = 6000)
    private String carDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Brand getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(Brand carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public BodyType getCarBodyType() {
        return carBodyType;
    }

    public void setCarBodyType(BodyType carBodyType) {
        this.carBodyType = carBodyType;
    }

    public String getCarImageUrl() {
        return carImageUrl;
    }

    public void setCarImageUrl(String carImageUrl) {
        this.carImageUrl = carImageUrl;
    }

    public Integer getCarYear() {
        return carYear;
    }

    public void setCarYear(Integer carYear) {
        this.carYear = carYear;
    }

    public Float getCarEngineVolume() {
        return carEngineVolume;
    }

    public void setCarEngineVolume(Float carEngineVolume) {
        this.carEngineVolume = carEngineVolume;
    }

    public GearBoxType getCarGearboxType() {
        return carGearboxType;
    }

    public void setCarGearboxType(GearBoxType carGearboxType) {
        this.carGearboxType = carGearboxType;
    }

    public String getCarDescription() {
        return carDescription;
    }

    public void setCarDescription(String carDescription) {
        this.carDescription = carDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CarDTO)) {
            return false;
        }

        CarDTO carDTO = (CarDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, carDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarDTO{" +
            "id=" + getId() +
            ", carBrand='" + getCarBrand() + "'" +
            ", carModel='" + getCarModel() + "'" +
            ", carBodyType='" + getCarBodyType() + "'" +
            ", carImageUrl='" + getCarImageUrl() + "'" +
            ", carYear=" + getCarYear() +
            ", carEngineVolume=" + getCarEngineVolume() +
            ", carGearboxType='" + getCarGearboxType() + "'" +
            ", carDescription='" + getCarDescription() + "'" +
            "}";
    }
}
