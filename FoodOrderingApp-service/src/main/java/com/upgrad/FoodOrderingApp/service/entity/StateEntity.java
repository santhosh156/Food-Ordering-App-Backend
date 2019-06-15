package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;

@Entity
@NamedQueries(
        {
                @NamedQuery(name = "stateByUuid", query = "select s from StateEntity s where s.uuid = :uuid"),
                @NamedQuery(name = "stateById", query = "select s from StateEntity s where s.id = :id"),
        }
)
@Table(name = "state")
public class StateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "state_name")
    private String stateName;

    public StateEntity(String uuid, String stateName) {
        this.uuid = uuid;
        this.stateName = stateName;
    }

    public StateEntity() {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
