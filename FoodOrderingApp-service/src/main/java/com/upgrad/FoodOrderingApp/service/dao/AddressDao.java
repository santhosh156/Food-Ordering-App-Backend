package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class AddressDao {

    @PersistenceContext
    private EntityManager entityManager;

    public AddressEntity getAddressById(final Long addressId) {
        try {
            return entityManager.createNamedQuery("addressById", AddressEntity.class).setParameter("id", addressId)
                    .getSingleResult();
        } catch(NoResultException nre) {
            return null;
        }

    }

    public AddressEntity getAddressByUuid(final String addressUuid) {
        try {
            return entityManager.createNamedQuery("addressByUuid", AddressEntity.class).setParameter("uuid", addressUuid)
                    .getSingleResult();
        } catch(NoResultException nre) {
            return null;
        }

    }

    public AddressEntity createAddress(AddressEntity addressEntity) {
        entityManager.persist(addressEntity);
        return addressEntity;
    }

    public AddressEntity deleteAddressByUuid(final AddressEntity addressEntity) {
        entityManager.remove(addressEntity);
        return addressEntity;
        /*try {
            AddressEntity ae = entityManager.createNamedQuery("deleteAddressByUuid", AddressEntity.class).setParameter("uuid", addressUuid)
                    .getSingleResult();
            ae.setActive(0);
            entityManager.persist(ae);
            return ae;
        } catch (NoResultException nre) {
            return null;
        }*/
    }

}
