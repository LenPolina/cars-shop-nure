package com.implemica.cart.model;


import lombok.*;


import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(uniqueConstraints = {@UniqueConstraint(name = "UserCar", columnNames = { "cart_user", "car_id"})})
public class Cart {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    Long id;

    @Column(name = "cart_user", nullable = false)
    String username;

    @Column(name = "car_id", nullable = false)
    Long carId;

    public Cart() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }
}
