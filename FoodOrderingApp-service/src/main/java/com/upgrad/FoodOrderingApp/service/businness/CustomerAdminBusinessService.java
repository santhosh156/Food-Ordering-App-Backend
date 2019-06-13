package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthTokenEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;

@Service
public class CustomerAdminBusinessService {

    @Autowired
    private CustomerDao customerDao;

    @Transactional
    public CustomerEntity updateCustomer (CustomerEntity updatedCustomerEntity, final String authorizationToken)
            throws AuthorizationFailedException, UpdateCustomerException {

        final ZonedDateTime now = ZonedDateTime.now();

        //get the customerAuthToken details from customerDao
        CustomerAuthTokenEntity customerAuthTokenEntity = customerDao.getCustomerAuthToken(authorizationToken);

        // Throw AuthorizationFailedException if the customer is not authorized
        if (customerAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
            // Throw AuthorizationFailedException if the customer is logged out
        } else if (customerAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
            // Throw AuthorizationFailedException if the customer session is expired
        } else if (now.isAfter(customerAuthTokenEntity.getExpiresAt()) ) {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }

        //get the customer Details using the customerUuid
        CustomerEntity customerEntity =  customerDao.getCustomerByUuid(customerAuthTokenEntity.getUuid());

        if (updatedCustomerEntity.getFirstName() == null) {
            throw new UpdateCustomerException("UCR-002", "First name field should not be empty");
        }


        // Now set the updated firstName and lastName and attach it to the customerEntity
        customerEntity.setFirstName(updatedCustomerEntity.getFirstName());
        customerEntity.setLastName(updatedCustomerEntity.getLastName());

        //called customerDao to merge the content and update in the database
        customerDao.updateCustomer(customerEntity);
        return customerEntity;

    }

}
