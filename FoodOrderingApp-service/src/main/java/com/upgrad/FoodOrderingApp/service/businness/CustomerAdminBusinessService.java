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

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

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

    @Transactional
    public CustomerEntity updateCustomerPassword (final String oldPassword, final String newPassword, final String authorizationToken)
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

        if (oldPassword == null || newPassword ==  null) {
            throw new UpdateCustomerException("UCR-003", "No field should be empty");
        }

        // Since the password stored in the database is encrypted, so we also encrypt the password entered by the customer
        // using the Salt attribute in the database
        // Call the encrypt() method in PasswordCryptographyProvider class for CryptographyProvider object
        final String encryptedPassword = cryptographyProvider.encrypt(oldPassword, customerEntity.getSalt());

        if(!encryptedPassword.equals(customerEntity.getPassword())) {
            throw new UpdateCustomerException("UCR-004", "Incorrect old password!");
        } else if (newPassword.length() < 8
                || !newPassword.matches(".*[0-9]{1,}.*")
                || !newPassword.matches(".*[A-Z]{1,}.*")
                || !newPassword.matches(".*[#@$%&*!^]{1,}.*")) {
            throw new UpdateCustomerException("UCR-001", "Weak password!");
        }

        // Now set the updated password and attach it to the customerEntity
        customerEntity.setPassword(newPassword);

        String[] encryptedText = cryptographyProvider.encrypt(customerEntity.getPassword());
        customerEntity.setSalt(encryptedText[0]);
        customerEntity.setPassword(encryptedText[1]);

        //called customerDao to merge the content and update in the database
        customerDao.updateCustomer(customerEntity);
        return customerEntity;
    }

}
