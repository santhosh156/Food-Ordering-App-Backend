package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantBusinessService {

    @Autowired
    private RestaurantDao restaurantDao;

    // A Method is for getAllRestaurants endpoint
    public List<RestaurantEntity> getAllRestaurants() {
        return restaurantDao.getAllRestaurants();
    }

    // A Method which takes the restaurantName as parameter for  getRestaurantsByName endpoint
    public List<RestaurantEntity> getRestaurantsByName(String restaurantName) {
        return restaurantDao.getRestaurantsByName(restaurantName);
    }

    // A Method which takes the categoryUUID as parameter for  getRestaurantByCategoryId endpoint
    public List<RestaurantCategoryEntity> getRestaurantByCategoryId(String categoryUUID) {
        return restaurantDao.getRestaurantByCategoryId(categoryUUID);
    }

    // A Method which takes the restaurantUUID as parameter for  getRestaurantByUUId endpoint
    public RestaurantEntity getRestaurantByUUId(String restaurantUUID) {
        return restaurantDao.getRestaurantByUUId(restaurantUUID);
    }
}
