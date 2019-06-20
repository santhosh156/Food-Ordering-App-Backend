package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.*;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.hibernate.internal.CriteriaImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private CustomerAdminBusinessService customerAdminBusinessService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private StateService stateService;

    @RequestMapping(method = RequestMethod.GET, path = "/order/coupon/{coupon_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CouponDetailsResponse> getCoupon(@RequestHeader("authorization")  final String authorization, @PathVariable("coupon_name") final String couponName)
            throws AuthorizationFailedException, CouponNotFoundException {

        String[] bearerToken = authorization.split( "Bearer ");

        if(couponName == null || couponName.isEmpty() || couponName.equalsIgnoreCase("\"\"")){
            throw new CouponNotFoundException("CPF-002", "Coupon name field should not be empty");
        }

        CouponEntity couponEntity = orderService.getCouponByName(couponName, bearerToken[1]);

        if (couponEntity == null) {
            throw new CouponNotFoundException("CPF-001", "No coupon by this name");
        }

        CouponDetailsResponse couponDetailsResponse = new CouponDetailsResponse().id(couponEntity.getUuid())
                .couponName(couponEntity.getCouponName()).percent(couponEntity.getPercent());

        return new ResponseEntity<CouponDetailsResponse>(couponDetailsResponse, HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.GET, path = "/order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getCustomerOrders(@RequestHeader("authorization") final String authorization)
            throws AuthorizationFailedException {

        String[] bearerToken = authorization.split( "Bearer ");

        // Gets all the past orders of the customer
        final List<OrdersEntity> ordersEntityList = orderService.getCustomerOrders(bearerToken[1]);

        List<OrderList> orderDetailsList = new ArrayList<OrderList>();

        for (OrdersEntity oe: ordersEntityList) {
            OrderList detail = new OrderList();

            detail.setId(UUID.fromString(oe.getUuid()));
            detail.setBill(oe.getBill());
            detail.setDiscount(oe.getDiscount());
            detail.setDate(oe.getDate().toString());

            // Getting coupon details of the order and adding to details
            CouponEntity couponEntity = couponService.getCouponById(oe.getCoupon().getId());

            OrderListCoupon orderListCoupon = new OrderListCoupon();
            orderListCoupon.setId(couponEntity.getUuid());
            orderListCoupon.setCouponName(couponEntity.getCouponName());
            orderListCoupon.setPercent(couponEntity.getPercent());

            detail.setCoupon(orderListCoupon);

            // Getting payment details of the order and adding to details
            PaymentEntity paymentEntity = paymentService.getPaymentById(oe.getPayment().getId());

            OrderListPayment orderListPayment = new OrderListPayment();
            orderListPayment.setId(UUID.fromString(paymentEntity.getUuid()));
            orderListPayment.setPaymentName(oe.getPayment().getPaymentName());

            detail.setPayment(orderListPayment);

            // Getting customer details of the order and adding it to details
            CustomerEntity customerEntity = customerAdminBusinessService.getCustomerById(oe.getCustomer().getId());

            OrderListCustomer orderListCustomer = new OrderListCustomer();
            orderListCustomer.setId(UUID.fromString(customerEntity.getUuid()));
            orderListCustomer.setFirstName(customerEntity.getFirstName());
            orderListCustomer.setLastName(customerEntity.getLastName());
            orderListCustomer.setEmailAddress(customerEntity.getEmail());
            orderListCustomer.setContactNumber(customerEntity.getContactNumber());

            detail.setCustomer(orderListCustomer);

            // Getting address details of the restaurant and adding it to details
            AddressEntity addressEntity = addressService.getAddressById(oe.getAddress().getId());

            OrderListAddress orderListAddress = new OrderListAddress();
            orderListAddress.setId(UUID.fromString(addressEntity.getUuid()));
            orderListAddress.setFlatBuildingName(addressEntity.getFlatBuilNumber());
            orderListAddress.setLocality(addressEntity.getLocality());
            orderListAddress.setCity(addressEntity.getCity());
            orderListAddress.setPincode(addressEntity.getPinCode());

            // Getting state details of the restaurant address and adding it to address and details
            StateEntity stateEntity = stateService.getStateById(oe.getAddress().getState().getId());

            OrderListAddressState orderListAddressState = new OrderListAddressState();
            orderListAddressState.setId(UUID.fromString(stateEntity.getUuid()));
            orderListAddressState.setStateName(stateEntity.getStateName());
            orderListAddress.setState(orderListAddressState);

            detail.setAddress(orderListAddress);
            orderDetailsList.add(detail);

        }

        return new ResponseEntity<>(orderDetailsList, HttpStatus.OK);

    }
}
