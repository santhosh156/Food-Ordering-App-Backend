package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomerAddressDao {

    @PersistenceContext
    private EntityManager entityManager;


    public List<AddressEntity> getAddressForCustomerByUuid(final String uuid) {
        try {
            CustomerEntity customerEntity = entityManager.createQuery("SELECT c FROM CustomerEntity c WHERE c.uuid = :uuid", CustomerEntity.class)
                    .setParameter("uuid", uuid).getSingleResult();

            List<CustomerAddressEntity> customerAddressEntities = entityManager.createQuery("SELECT ca FROM CustomerAddressEntity ca WHERE ca.customerId = :id", CustomerAddressEntity.class)
                    .setParameter("id", customerEntity.getId()).getResultList();
            List<Long> ids = new ArrayList<>();

            for (CustomerAddressEntity cae : customerAddressEntities) {
                ids.add(cae.getAddressId());
            }

            List<AddressEntity> addressEntitiesList = entityManager.createQuery(
                    "SELECT a FROM AddressEntity a WHERE a.id in :addressIds AND a.active = 1", AddressEntity.class)
                    .setParameter("addressIds", ids).getResultList();

            return addressEntitiesList;
        } catch (NoResultException nre) {
            return new ArrayList<>();
        }
    }
}