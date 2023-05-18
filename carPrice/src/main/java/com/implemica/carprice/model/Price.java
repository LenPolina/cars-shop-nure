package com.implemica.carprice.model;


//import jakarta.persistence.*;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Setter
@Getter
@Builder
public class Price {

    public Price() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getCarPrice() {
        return carPrice;
    }

    public void setCarPrice(BigDecimal carPrice) {
        this.carPrice = carPrice;
    }

    @Id
    @Column(name = "car_id")
    Long id;

    @Column(name = "car_price", nullable = false)
    BigDecimal carPrice;

    public Price(Long id, BigDecimal carPrice) {
        this.id = id;
        this.carPrice = carPrice;
    }
}
