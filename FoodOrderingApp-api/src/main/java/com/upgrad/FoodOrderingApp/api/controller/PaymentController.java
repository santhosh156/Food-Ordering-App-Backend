package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.PaymentListResponse;
import com.upgrad.FoodOrderingApp.api.model.PaymentResponse;
import com.upgrad.FoodOrderingApp.service.businness.PaymentService;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     *
     * @return Payment methods list
     *
     */
    @RequestMapping(method = RequestMethod.GET, path = "/payment", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<PaymentListResponse> getAllPaymentMethods() {

        // Arraylist to get the records from Payment table
        List<PaymentEntity> paymentEntityList = new ArrayList<>();

        // All records retrieved added to the arraylist
        paymentEntityList.addAll(paymentService.getAllPaymentMethods());

        // List to store the payment response list
        PaymentListResponse paymentListResponse = new PaymentListResponse();

        // Looping thru the paymentEntityList to add each record to Payment response
        for (PaymentEntity paymentEntity : paymentEntityList) {

            PaymentResponse paymentResponse = new PaymentResponse();

            paymentResponse.setId(UUID.fromString(paymentEntity.getUuid()));
            paymentResponse.setPaymentName(paymentEntity.getPaymentName());
            paymentListResponse.addPaymentMethodsItem(paymentResponse);

        }

        // Returns the PaymentListResponse with OK https status
        return new ResponseEntity<PaymentListResponse>(paymentListResponse, HttpStatus.OK);

    }
}