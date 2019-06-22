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

    public void createCustomerAddress(CustomerAddressEntity customerAddressEntity) {
        entityManager.persist(customerAddressEntity);
    }

    public List<AddressEntity> getAddressForCustomerByUuid(final String uuid) {
        try {
            CustomerEntity customerEntity = entityManager.createNamedQuery("customerByUuid", CustomerEntity.class)
                    .setParameter("uuid", uuid).getSingleResult();

            List<CustomerAddressEntity> customerAddressEntities = entityManager.createNamedQuery("customerAddressesListByCustomerId", CustomerAddressEntity.class)
                    .setParameter("customer", customerEntity).getResultList();

            if (customerAddressEntities.size() == 0) {
                return null;
            }

            List<Long> ids = new ArrayList<>();

            for (CustomerAddressEntity cae : customerAddressEntities) {
                ids.add(cae.getAddress().getId());
            }

            List<AddressEntity> addressEntitiesList = entityManager.createQuery(
                    "SELECT a FROM AddressEntity a WHERE a.id in :addressIds AND a.active = 1 order by a.id desc", AddressEntity.class)
                    .setParameter("addressIds", ids).getResultList();

            return addressEntitiesList;
        } catch (NoResultException nre) {
            return null;
        }
    }

    public CustomerAddressEntity getCustAddressByCustIdAddressId(final CustomerEntity customerEntity, final AddressEntity addressEntity) {
        try {
            return entityManager.createNamedQuery("custAddressByCustIdAddressId", CustomerAddressEntity.class)
                    .setParameter("customer", customerEntity).setParameter( "address", addressEntity)
                    .getSingleResult();
        } catch(NoResultException nre) {
            return null;
        }
    }
}