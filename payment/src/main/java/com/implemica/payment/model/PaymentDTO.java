package com.implemica.payment.model;


import lombok.*;

import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;


@Setter
@Getter
@Builder

@Table(uniqueConstraints = {@UniqueConstraint(name = "payment_order", columnNames = { "order_id", "payment_id"})})
public class PaymentDTO {
    @Min(value = 1, message = "Car id must be more than one.")
    Long id;

    @NotNull(message = "Order Id is required.")
    Long orderId;

    Date paymentDate;

    PaymentStatus paymentStatus;

    @Min(1)
    @NotNull(message = "Payment total sum is required.")
    Double paymentTotalSum;

    public PaymentDTO() {
    }

    public PaymentDTO(Long id, Long orderId, Date paymentDate, PaymentStatus paymentStatus, Double paymentTotalSum) {
        this.id = id;
        this.orderId = orderId;
        this.paymentDate = paymentDate;
        this.paymentStatus = paymentStatus;
        this.paymentTotalSum = paymentTotalSum;
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
