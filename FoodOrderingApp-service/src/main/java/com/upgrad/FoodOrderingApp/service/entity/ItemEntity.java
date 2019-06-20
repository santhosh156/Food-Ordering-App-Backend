package com.upgrad.FoodOrderingApp.service.entity;

import com.upgrad.FoodOrderingApp.api.model.ItemList;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table(name = "item")
public class ItemEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @NotNull
    @Size(max = 200)
    private String uuid;

    @Column(name = "ITEM_NAME")
    @NotNull
    @Size(max = 30)
    private String itemName;

    @Column(name = "PRICE")
    private Integer price;

    private ItemList.ItemTypeEnum type;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public ItemList.ItemTypeEnum getType() {
        return type;
    }

    public void setType(ItemList.ItemTypeEnum type) {
        this.type = type;
    }
}
