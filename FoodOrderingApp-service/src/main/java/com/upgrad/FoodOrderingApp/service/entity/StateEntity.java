package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
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
    @Column(name = "id")
    private Long id;

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "state_name")
    private String stateName;

    public StateEntity(UUID uuid, String stateName) {
        this.uuid = uuid;
        this.stateName = stateName;
    }

    public StateEntity() {
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
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
