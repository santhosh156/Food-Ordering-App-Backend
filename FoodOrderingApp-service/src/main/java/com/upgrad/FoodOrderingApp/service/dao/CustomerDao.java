package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthTokenEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CustomerDao {

    @PersistenceContext
    private EntityManager entityManager;

    // Creates customer by persisting the record in the database
    public CustomerEntity createCustomer(CustomerEntity customerEntity) {
        entityManager.persist(customerEntity);
        return customerEntity;
    }

    // Gets the customer details from the database based on id
    public CustomerEntity getCustomerById(final Integer customerId) {
        try {
            return entityManager.createNamedQuery("customerById", CustomerEntity.class).setParameter("id", customerId)
                    .getSingleResult();
        } catch(NoResultException nre) {
            return null;
        }
    }

    // Gets the customer details from the database based on uuid
    public CustomerEntity getCustomerByUuid(final String uuid) {
        try {
            return entityManager.createNamedQuery("customerByUuid", CustomerEntity.class).setParameter("uuid", uuid)
                    .getSingleResult();
        } catch(NoResultException nre) {
            return null;
        }
    }

    // Gets the customer details from the database based on contact number
    public CustomerEntity getCustomerByContactNumber(final String customerContactNumber) {
        try {
            return entityManager.createNamedQuery("customerByContactNumber", CustomerEntity.class).setParameter("contactNumber", customerContactNumber)
                    .getSingleResult();
        } catch(NoResultException nre) {
            return null;
        }
    }

    // Creates auth token by persisting the record in the database
    public CustomerAuthTokenEntity createAuthToken(final CustomerAuthTokenEntity customerAuthTokenEntity) {
        entityManager.persist(customerAuthTokenEntity);
        return customerAuthTokenEntity;
    }

    // Updates the customer details to the database
    public void updateCustomer(final CustomerEntity updatedCustomerEntity) {
        entityManager.merge(updatedCustomerEntity);
    }

    //
    public void updateCustomerAuth(final CustomerAuthTokenEntity customerAuthTokenEntity) {
        entityManager.merge(customerAuthTokenEntity);
    }

    public CustomerEntity deleteCustomer(final CustomerEntity customerEntity){
        entityManager.remove(customerEntity);
        return customerEntity;
    }

    public CustomerAuthTokenEntity getCustomerAuthToken(final String accessToken) {
        try {
            return entityManager.createNamedQuery("customerAuthTokenByAccessToken", CustomerAuthTokenEntity.class)
                    .setParameter("accessToken", accessToken).getSingleResult();
        } catch(NoResultException nre) {
            return null;
        }
    }

    public CustomerAuthTokenEntity getCustomerAuthTokenById(final Long customerId) {
        try {
            return entityManager.createNamedQuery("customerAuthTokenById", CustomerAuthTokenEntity.class)
                    .setParameter("customer", customerId).getSingleResult();
        } catch(NoResultException nre) {
            return null;
        }
    }


}
