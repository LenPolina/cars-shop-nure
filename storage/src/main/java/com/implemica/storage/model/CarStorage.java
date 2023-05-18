package com.implemica.storage.model;



import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


@Entity
@Setter
@Getter
public class CarStorage {

    @Id
    @Column(name = "car_id")
    Long id;

    @Min(0)
    @Max(100)
    @Column(name = "car_number", nullable = false)
    Long carNumber;

    public CarStorage() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(Long carNumber) {
        this.carNumber = carNumber;
    }
}
