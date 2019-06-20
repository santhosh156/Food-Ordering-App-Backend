package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.dao.OrderDao;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthTokenEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrdersEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    CustomerAdminBusinessService customerAdminBusinessService;

    @Autowired
    CustomerDao customerDao;

    @Transactional
    public CouponEntity getCouponByName(String couponName, final String authorizationToken) throws AuthorizationFailedException {

        // Gets the customerAuthToken details from customerDao
        CustomerAuthTokenEntity customerAuthTokenEntity = customerDao.getCustomerAuthToken(authorizationToken);

        // Validates the access token retrieved from database
        customerAdminBusinessService.validateAccessToken(customerAuthTokenEntity);

        return orderDao.getCouponByName(couponName);
    }

    @Transactional
    public List<OrdersEntity> getCustomerOrders(final String authorizationToken) throws AuthorizationFailedException {

        // Gets the customerAuthToken details from customerDao
        CustomerAuthTokenEntity customerAuthTokenEntity = customerDao.getCustomerAuthToken(authorizationToken);

        // Validates the access token retrieved from database
        customerAdminBusinessService.validateAccessToken(customerAuthTokenEntity);

        return orderDao.getCustomerOrders(customerAuthTokenEntity.getId());
    }
}
