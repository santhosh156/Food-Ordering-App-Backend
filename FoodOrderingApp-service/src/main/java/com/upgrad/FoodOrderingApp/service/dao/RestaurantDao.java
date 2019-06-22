package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestaurantDao  {

    @PersistenceContext
    private EntityManager entityManager;

    public List<RestaurantEntity> getAllRestaurants() {
        try {
            return entityManager.createNamedQuery("allRestaurants", RestaurantEntity.class).getResultList();
        } catch(NoResultException nre) {
            return null;
        }
    }

    public List<RestaurantEntity> getRestaurantsByName(String restaurantName) {
        try {
             return entityManager.createNamedQuery("findByName", RestaurantEntity.class).setParameter("restaurantName","%" + restaurantName.toLowerCase() + "%" ).getResultList();
        } catch(NoResultException nre) {
            return null;
        }
    }

    public List<RestaurantCategoryEntity> getRestaurantByCategoryId(final Long categoryID) {
        try {
            return entityManager.createNamedQuery("restaurantsByCategoryId", RestaurantCategoryEntity.class).setParameter("id",categoryID).getResultList();
        } catch(NoResultException nre) {
            return null;
        }
    }

    public RestaurantEntity getRestaurantByUUId(String restaurantUUID) {
        try {
            return entityManager.createNamedQuery("findRestaurantByUUId", RestaurantEntity.class).setParameter("restaurantUUID",restaurantUUID.toLowerCase()).getSingleResult();
        } catch(NoResultException nre) {
            return null;
        }
    }

    public void updateRestaurant(final RestaurantEntity updatedRestaurantEntity) {
        entityManager.merge(updatedRestaurantEntity);
    }
  
}
