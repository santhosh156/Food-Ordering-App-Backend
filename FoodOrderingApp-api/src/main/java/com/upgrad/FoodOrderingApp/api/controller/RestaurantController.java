package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponse;
import com.upgrad.FoodOrderingApp.api.model.RestaurantList;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantBusinessService;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class RestaurantController {

    @Autowired
    RestaurantBusinessService restaurantBusinessService;

    @RequestMapping(method = RequestMethod.GET, path = "/restaurant", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getAllRestaurants() {

        //getting the list of all questions using the bearertoken as parameter
        final List<RestaurantEntity> allRestaurants = restaurantBusinessService.getAllRestaurants();

        //adding the list of questions to the question detail response
        List<RestaurantDetailsResponse> details = new ArrayList<RestaurantDetailsResponse>();
        for (RestaurantEntity n: allRestaurants) {
            RestaurantDetailsResponse detail = new RestaurantDetailsResponse();

            detail.setRestaurantName(n.getRestaurantName());
            detail.setPhotoURL(n.getPhotoUrl());
            detail.setCustomerRating(n.getCustomerRating());
            detail.setAveragePrice(n.getAvgPriceForTwo());
            detail.setNumberCustomersRated(n.getNumCustomersRated());

            details.add(detail);

        }

        return new ResponseEntity<>(details, HttpStatus.OK);
    }
}
