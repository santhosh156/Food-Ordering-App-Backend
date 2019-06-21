package com.upgrad.FoodOrderingApp.service.entity;

import com.upgrad.FoodOrderingApp.api.model.ItemList;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "item")
@NamedQueries(
        {
                @NamedQuery(name = "itemByUuid", query = "select i from ItemEntity i where i.uuid=:uuid"),
                @NamedQuery(name = "itemById", query = "select i from ItemEntity i where i.id=:id")
        }
)

public class ItemEntity implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "UUID")
    @Size(max = 200)
    private String uuid;

    @Column(name = "ITEM_NAME")
    @NotNull
    @Size(max = 30)
    private String itemName;

    @Column(name="PRICE")
    @NotNull
    private Integer price;

    /*@Column(name = "TYPE")
    @NotNull
    @Size(max = 10)
    private String type;*/

    private ItemList.ItemTypeEnum type;

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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    /*public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }*/

    public ItemList.ItemTypeEnum getType() {
        return type;
    }

    public void setType(ItemList.ItemTypeEnum type) {
        this.type = type;
    }
}

