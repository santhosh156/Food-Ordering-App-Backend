package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@NamedQueries(
        {
                @NamedQuery(name = "categoryByUuid", query = "select c from CategoryEntity c where c.uuid=:uuid"),
                @NamedQuery(name = "categoryById", query = "select c from CategoryEntity c where c.id=:id"),
                @NamedQuery(name = "allCategories", query = "select c from CategoryEntity c order by c.categoryName")
        }
)

public class CategoryEntity implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "UUID")
    @Size(max = 200)
    private String uuid;

    @Column(name = "CATEGORY_NAME")
    private String categoryName;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id", referencedColumnName="id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "item_id", referencedColumnName="id", nullable = false)
    )
    private List<ItemEntity> itemEntities =new ArrayList<>();

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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<ItemEntity> getItemEntities() {
                return itemEntities;
    }

    public void setItemEntities(List<ItemEntity> itemEntities) {
                this.itemEntities = itemEntities;
    }

}



