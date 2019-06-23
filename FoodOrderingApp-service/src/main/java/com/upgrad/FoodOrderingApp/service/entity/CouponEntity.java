package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "coupon")
@NamedQueries(
        {
                @NamedQuery(name = "couponByUuid", query = "select c from CouponEntity c where c.uuid=:uuid"),
                @NamedQuery(name = "couponById", query = "select c from CouponEntity c where c.id=:id"),
                @NamedQuery(name = "couponByName", query = "select c from CouponEntity c where c.couponName=:couponName")
        }
)

public class CouponEntity implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "UUID")
    @Size(max = 200)
    private String uuid;

    @Column(name = "COUPON_NAME")
    private String couponName;

    @Column(name="PERCENT")
    @NotNull
    private Integer percent;

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

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }
}
