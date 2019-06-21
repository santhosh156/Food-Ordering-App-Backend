package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "payment")
@NamedQueries(
        {
                @NamedQuery(name = "allPaymentMethods", query = "select p from PaymentEntity p "),
                @NamedQuery(name = "paymentById", query = "select p from PaymentEntity p where p.id=:id"),
                @NamedQuery(name = "paymentByUuid", query = "select p from PaymentEntity p where p.uuid=:uuid"),
        }
)


public class PaymentEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "UUID")
    @NotNull
    @Size(max = 200)
    private String uuid;

    @Column(name="PAYMENT_NAME")
    @NotNull
    @Size(max = 255)
    private String paymentName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }
}

