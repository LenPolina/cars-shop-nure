package com.implemica.payment.model;


import lombok.*;

import javax.persistence.*;
import java.util.Date;


import static com.implemica.payment.model.PaymentStatus.UNPAID;

@Entity
@Setter
@Getter
@Table(uniqueConstraints = {@UniqueConstraint(name = "payment_order", columnNames = { "order_id", "payment_id"})})
public class CustomPayment {

    @Id
    @Column(name = "payment_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    Long id;

    @Column(name = "order_id", nullable = false)
    Long orderId;

    @Column(name = "payment_date", nullable = false)
    Date paymentDate;

    @Column(name = "payment_status", nullable = false)
    PaymentStatus paymentStatus = UNPAID;

    @Column(name = "total_price", nullable = false)
    Double paymentTotalSum;

    public CustomPayment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Double getPaymentTotalSum() {
        return paymentTotalSum;
    }

    public void setPaymentTotalSum(Double paymentTotalSum) {
        this.paymentTotalSum = paymentTotalSum;
    }
}
