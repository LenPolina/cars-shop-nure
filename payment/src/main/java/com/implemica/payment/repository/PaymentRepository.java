package com.implemica.payment.repository;


import com.implemica.payment.model.CustomPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PaymentRepository extends JpaRepository<CustomPayment, Long> {
}
