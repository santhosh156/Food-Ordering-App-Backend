package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class AddressDao {

    @PersistenceContext
    private EntityManager entityManager;

    public AddressEntity createAddress(AddressEntity AddressEntity) {
        entityManager.persist(AddressEntity);
        return AddressEntity;
    }

    public AddressEntity getAllAddressOfCustomerByUuid(final String uuid) {
        try {
            return entityManager.createNamedQuery("addressByUuid", AddressEntity.class).setParameter("uuid", uuid)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}