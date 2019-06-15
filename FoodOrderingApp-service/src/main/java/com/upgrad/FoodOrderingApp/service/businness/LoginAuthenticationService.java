package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthTokenEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class LoginAuthenticationService {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthTokenEntity authenticate(final String contactNumber , final String password) throws AuthenticationFailedException {
        //Call the getCustomerByContactNumber method in CustomerDao class for CustomerDao object and pass contactNumber as argument
        // Receive the value returned by getCustomerByContactNumber() method in CustomerEntity type object(name it as customerEntity)
        CustomerEntity customerEntity = customerDao.getCustomerByContactNumber(contactNumber);
        if(customerEntity == null) {
            throw new AuthenticationFailedException("ATH-001", "This contact number has not been registered!");
        }

        //After that you have got customerEntity from customer table, we need to compare the password entered by the customer
        // with the password stored in the database
        //Since the password stored in the database is encrypted, so we also encrypt the password entered by the customer
        // using the Salt attribute in the database
        //Call the encrypt() method in PasswordCryptographyProvider class for CryptographyProvider object

        final String encryptedPassword = cryptographyProvider.encrypt(password, customerEntity.getSalt());
        //Now encryptedPassword contains the password entered by the customer in encrypted form
        //And customerEntity.getPassword() gives the password stored in the database in encrypted form
        //We compare both the passwords (Note that in this Assessment we are assuming that the credentials are correct)
        if(encryptedPassword.equals(customerEntity.getPassword())) {
            //Implementation of JwtTokenProvider class
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
            //Now CustomerAuthTokenEntity type object is to be persisted in a database
            //Declaring an object customerAuthTokenEntity of type CustomerAuthTokenEntity and setting its attributes
            CustomerAuthTokenEntity customerAuthTokenEntity = new CustomerAuthTokenEntity();
            customerAuthTokenEntity.setCustomer(customerEntity);
            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(8);

            customerAuthTokenEntity.setAccessToken(jwtTokenProvider.generateToken(customerAuthTokenEntity.getUuid(), now, expiresAt));

            customerAuthTokenEntity.setLoginAt(now);
            customerAuthTokenEntity.setExpiresAt(expiresAt);
            customerAuthTokenEntity.setUuid(customerEntity.getUuid());

            //Call the createAuthToken() method in CustomerDao class for customerDao
            //Pass customerAuthTokenEntity as an argument

            customerDao.createAuthToken(customerAuthTokenEntity);

            //To update the last login time of customer
            //Carefully read how to update the existing record in a database(will be asked in later Assessments)
            //When the persistence context is closed the entity becomes detached and any further changes to the entity will not be saved
            //You need to associate the detached entity with a persistence context through merge() method to update the entity
            //updateCustomer() method in CustomerDao class calls the merge() method to update the customerEntity

            customerDao.updateCustomer(customerEntity);
            return customerAuthTokenEntity;

        }
        else{
            //throw exception
            throw new AuthenticationFailedException("ATH-002", "Invalid Credentials");
        }

    }



}
