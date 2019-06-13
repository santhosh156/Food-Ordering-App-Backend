package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.LoginResponse;
import com.upgrad.FoodOrderingApp.api.model.LogoutResponse;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerRequest;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerResponse;
import com.upgrad.FoodOrderingApp.service.businness.LoginAuthenticationService;
import com.upgrad.FoodOrderingApp.service.businness.LogoutBusinessService;
import com.upgrad.FoodOrderingApp.service.businness.SignupBusinessService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthTokenEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class CustomerController {

    @Autowired
    private SignupBusinessService signupBusinessService;

    @Autowired
    private LoginAuthenticationService authenticationService;

    @Autowired
    private LogoutBusinessService logoutBusinessService;

    @RequestMapping(method=RequestMethod.POST, path="/customer/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupCustomerResponse> signup(final SignupCustomerRequest signupCustomerRequest) throws SignUpRestrictedException {

        final CustomerEntity customerEntity = new CustomerEntity();

        customerEntity.setUuid(UUID.randomUUID().toString());
        customerEntity.setFirstName(signupCustomerRequest.getFirstName());
        customerEntity.setLastName(signupCustomerRequest.getLastName());
        customerEntity.setEmail(signupCustomerRequest.getEmailAddress());
        customerEntity.setContactNumber(signupCustomerRequest.getContactNumber());
        customerEntity.setPassword(signupCustomerRequest.getPassword());

        final CustomerEntity createdCustomerEntity = signupBusinessService.signup(customerEntity);
        SignupCustomerResponse customerResponse = new SignupCustomerResponse().id(createdCustomerEntity.getUuid()).status("CUSTOMER SUCCESSFULLY REGISTERED");

        return new ResponseEntity<SignupCustomerResponse>(customerResponse, HttpStatus.CREATED);

    }


    @RequestMapping(method = RequestMethod.POST , path="/customer/login" ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LoginResponse> login(@RequestHeader("authorization")  final String authorization) throws AuthenticationFailedException {

        // Validation to check whether the authorization starts with Basic
        if (authorization == null || !authorization.startsWith("Basic ")) {
            throw new AuthenticationFailedException("ATH-003", "Incorrect format of decoded customer name and password");
        }

        byte[] decode = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
        String decodedText = new String(decode);

        // Validation to check whether the format is digits:password
        if (!decodedText.matches("([0-9]+):(.+?)")) {
            throw new AuthenticationFailedException("ATH-003", "Incorrect format of decoded customer name and password");
        }
        String[] decodedArray = decodedText.split(":");

        CustomerAuthTokenEntity customerAuthToken = authenticationService.authenticate(decodedArray[0] , decodedArray[1]);
        CustomerEntity customer = customerAuthToken.getCustomer();

        LoginResponse authorizedCustomerResponse = new LoginResponse().id(customer.getUuid()).firstName(customer.getFirstName())
                .lastName(customer.getLastName()).emailAddress(customer.getEmail()).contactNumber(customer.getContactNumber())
                .message("LOGGED IN SUCCESSFULLY");

        HttpHeaders headers = new HttpHeaders();
        headers.add("access-token", customerAuthToken.getAccessToken());
        headers.add("access-control-expose-headers", "access-token");
        return new ResponseEntity<LoginResponse>(authorizedCustomerResponse, headers, HttpStatus.OK);
    }

    @RequestMapping(method= RequestMethod.POST, path="/customer/logout", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LogoutResponse>signout(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {

        String[] bearerToken = authorization.split( "Bearer ");
        final CustomerAuthTokenEntity customerAuthTokenEntity = logoutBusinessService.logout(bearerToken[1]);
        CustomerEntity customerEntity = customerAuthTokenEntity.getCustomer();

        LogoutResponse logoutResponse = new LogoutResponse().id(customerEntity.getUuid()).message("LOGGED OUT SUCCESSFULLY");

        return new ResponseEntity<LogoutResponse>(logoutResponse, HttpStatus.OK);

    }
}