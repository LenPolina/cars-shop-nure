package com.implemica.application.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.implemica.application.domain.enumeration.BodyType;
import com.implemica.application.domain.enumeration.Brand;
import com.implemica.application.domain.enumeration.GearBoxType;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * A Car.
 */
@Entity
@Table(name = "car")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "car_brand", nullable = false)
    private Brand carBrand;

    @NotNull
    @Size(min = 2, max = 40)
    @Column(name = "car_model", length = 40, nullable = false)
    private String carModel;

    @Enumerated(EnumType.STRING)
    @Column(name = "car_body_type")
    private BodyType carBodyType;

    @Column(name = "car_image_url")
    private String carImageUrl;

    @NotNull
    @Min(value = 1900)
    @Max(value = 2100)
    @Column(name = "car_year", nullable = false)
    private Integer carYear;

    @DecimalMin(value = "0")
    @DecimalMax(value = "30.0")
    @Column(name = "car_engine_volume")
    private Float carEngineVolume;

    @Enumerated(EnumType.STRING)
    @Column(name = "car_gearbox_type")
    private GearBoxType carGearboxType;

    @Size(min = 20, max = 6000)
    @Column(name = "car_description", length = 6000)
    private String carDescription;

    @OneToMany(mappedBy = "car")
    @JsonIgnoreProperties(value = { "car" }, allowSetters = true)
    private Set<Option> options = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Car id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Brand getCarBrand() {
        return this.carBrand;
    }

    public Car carBrand(Brand carBrand) {
        this.setCarBrand(carBrand);
        return this;
    }

    public void setCarBrand(Brand carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarModel() {
        return this.carModel;
    }

    public Car carModel(String carModel) {
        this.setCarModel(carModel);
        return this;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public BodyType getCarBodyType() {
        return this.carBodyType;
    }

    public Car carBodyType(BodyType carBodyType) {
        this.setCarBodyType(carBodyType);
        return this;
    }

    public void setCarBodyType(BodyType carBodyType) {
        this.carBodyType = carBodyType;
    }

    public String getCarImageUrl() {
        return this.carImageUrl;
    }

    public Car carImageUrl(String carImageUrl) {
        this.setCarImageUrl(carImageUrl);
        return this;
    }

    public void setCarImageUrl(String carImageUrl) {
        this.carImageUrl = carImageUrl;
    }

    public Integer getCarYear() {
        return this.carYear;
    }

    public Car carYear(Integer carYear) {
        this.setCarYear(carYear);
        return this;
    }

    public void setCarYear(Integer carYear) {
        this.carYear = carYear;
    }

    public Float getCarEngineVolume() {
        return this.carEngineVolume;
    }

    public Car carEngineVolume(Float carEngineVolume) {
        this.setCarEngineVolume(carEngineVolume);
        return this;
    }

    public void setCarEngineVolume(Float carEngineVolume) {
        this.carEngineVolume = carEngineVolume;
    }

    public GearBoxType getCarGearboxType() {
        return this.carGearboxType;
    }

    public Car carGearboxType(GearBoxType carGearboxType) {
        this.setCarGearboxType(carGearboxType);
        return this;
    }

    public void setCarGearboxType(GearBoxType carGearboxType) {
        this.carGearboxType = carGearboxType;
    }

    public String getCarDescription() {
        return this.carDescription;
    }

    public Car carDescription(String carDescription) {
        this.setCarDescription(carDescription);
        return this;
    }

    public void setCarDescription(String carDescription) {
        this.carDescription = carDescription;
    }

    public Set<Option> getOptions() {
        return this.options;
    }

    public void setOptions(Set<Option> options) {
        if (this.options != null) {
            this.options.forEach(i -> i.setCar(null));
        }
        if (options != null) {
            options.forEach(i -> i.setCar(this));
        }
        this.options = options;
    }

    public Car options(Set<Option> options) {
        this.setOptions(options);
        return this;
    }

    public Car addOption(Option option) {
        this.options.add(option);
        option.setCar(this);
        return this;
    }

    public Car removeOption(Option option) {
        this.options.remove(option);
        option.setCar(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Car)) {
            return false;
        }
        return id != null && id.equals(((Car) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Car{" +
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
