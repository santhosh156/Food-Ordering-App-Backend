package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@NamedQueries(
        {
                @NamedQuery(name = "categoryById", query = "select c from CategoryEntity c where c.id = :id"),
                @NamedQuery(name = "categoryByUUId", query = "select c from CategoryEntity c where c.uuid = :categoryUUID"),
                @NamedQuery(name = "allCategories", query = "select c from CategoryEntity c order by c.categoryName")
        }
)
public class CategoryEntity {

        @Id
        @Column(name = "ID")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(name = "UUID")
        @NotNull
        @Size(max = 200)
        private String uuid;

        @Column(name = "CATEGORY_NAME")
        @NotNull
        @Size(max = 255)
        private String categoryName;

        @OneToMany(cascade = CascadeType.ALL)
        @JoinTable(name = "category_item",
                joinColumns = @JoinColumn(name = "category_id", referencedColumnName="id", nullable = false),
                inverseJoinColumns = @JoinColumn(name = "item_id", referencedColumnName="id", nullable = false)
        )
        private List<ItemEntity> itemEntities =new ArrayList<>();

        public Integer getId() {
                return id;
        }

        public String getUuid() {
                return uuid;
        }

        public void setUuid(String uuid) {
                this.uuid = uuid;
        }

        public List<ItemEntity> getItemEntities() {
                return itemEntities;
        }

        public void setItemEntities(List<ItemEntity> itemEntities) {
                this.itemEntities = itemEntities;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public String getCategoryName() {
                return categoryName;
        }

        public void setCategoryName(String categoryName) {
                this.categoryName = categoryName;
        }
}
