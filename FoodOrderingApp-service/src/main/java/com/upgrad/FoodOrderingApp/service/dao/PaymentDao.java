package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class PaymentDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<PaymentEntity> getAllPaymentMethods(){

        try {
            return this.entityManager.createNamedQuery("allPaymentMethods", PaymentEntity.class).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
