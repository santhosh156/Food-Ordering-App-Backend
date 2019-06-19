package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
@NamedQueries(
        {
                @NamedQuery(name = "stateByUuid", query = "select s from StateEntity s where s.uuid = :uuid"),
                @NamedQuery(name = "stateById", query = "select s from StateEntity s where s.id = :id"),
                @NamedQuery(name = "allStates", query = "select s from StateEntity s"),
        }
)

@Table(name = "state")
public class StateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "UUID")
    @Size(max = 200)
    private String uuid;

    @Column(name = "STATE_NAME")
    private String stateName;

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

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
