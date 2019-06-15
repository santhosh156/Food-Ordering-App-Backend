package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "state")
@NamedQueries(
        {
                @NamedQuery(name = "stateById", query = "select s from StateEntity s where s.id = :id"),
        }
)
public class CategoryEntity {
}
