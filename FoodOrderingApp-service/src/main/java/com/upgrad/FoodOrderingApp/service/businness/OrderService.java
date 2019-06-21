package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.dao.OrderDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private CustomerAdminBusinessService customerAdminBusinessService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private PaymentService paymentService;

    @Transactional
    public CouponEntity getCouponByName(String couponName, final String authorizationToken) throws AuthorizationFailedException {

        // Validates the access token retrieved from database
        customerAdminBusinessService.validateAccessToken(authorizationToken);
        return orderDao.getCouponByName(couponName);
    }

    @Transactional
    public List<OrdersEntity> getCustomerOrders(final String authorizationToken) throws AuthorizationFailedException {

        // Gets the customerAuthToken details from customerDao
        CustomerAuthTokenEntity customerAuthTokenEntity = customerAdminBusinessService.getCustomerAuthToken(authorizationToken);

        // Validates the access token retrieved from database
        customerAdminBusinessService.validateAccessToken(authorizationToken);

        return orderDao.getCustomerOrders(customerAuthTokenEntity.getId());
    }

    @Transactional
    public OrdersEntity saveOrder(OrdersEntity ordersEntity, final String authorizationToken)
            throws AuthorizationFailedException, CouponNotFoundException, AddressNotFoundException, PaymentMethodNotFoundException, RestaurantNotFoundException {

        // Validates the provided access token
        customerAdminBusinessService.validateAccessToken(authorizationToken);

        // Throws CouponNotFoundException if coupon not found
        if (ordersEntity.getCoupon() == null) {
            throw new CouponNotFoundException("CPF-002", "No coupon by this id");
        // Throws AddressNotFoundException if address not found
        } else if (ordersEntity.getAddress() == null) {
            throw new AddressNotFoundException("ANF-003", "No address by this id");
        // Throws PaymentMethodNotFoundException id payment method not found
        } else if (ordersEntity.getPayment() ==  null) {
            throw new PaymentMethodNotFoundException("PNF-002", "No payment method found by this id");
        } else if (ordersEntity.getRestaurant() == null) {
            throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
        }

        return orderDao.saveOrder(ordersEntity);
    }
}
