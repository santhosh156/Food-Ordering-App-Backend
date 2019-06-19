package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "payment")
@NamedQueries(
        {
                @NamedQuery(name = "paymentByUuid", query = "select p from PaymentEntity p where p.uuid=:uuid"),
                @NamedQuery(name = "paymentById", query = "select p from PaymentEntity p where p.id=:id"),
                @NamedQuery(name = "paymentMethods", query = "select p from PaymentEntity p ")
        }
)

public class PaymentEntity implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "UUID")
    @Size(max = 200)
    private String uuid;

    @Column(name = "PAYMENT_NAME")
    private String paymentName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
