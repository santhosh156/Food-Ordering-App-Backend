package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerAddressDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.dao.StateDao;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthTokenEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class AddressService {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private CustomerAddressDao customerAddressDao;

    @Autowired
    private AddressDao addressDao;

    @Autowired
    private StateDao stateDao;

    public StateEntity getStateByUUID(String uuid) {
        return stateDao.getStateByUuid(uuid);
    }

    public StateEntity getStateById(Long stateId) {
        return stateDao.getStateById(stateId);
    }

    @Transactional
    public AddressEntity saveAddress(AddressEntity addressEntity, String bearerToken) throws AuthorizationFailedException, SaveAddressException, AddressNotFoundException {
        validateAccessToken(bearerToken);

        if (addressEntity.getCity() == null || addressEntity.getCity().isEmpty() ||
                addressEntity.getStateId() == null ||
                addressEntity.getFlatBuildingNumber() == null || addressEntity.getFlatBuildingNumber().isEmpty() ||
                addressEntity.getLocality() == null || addressEntity.getLocality().isEmpty() ||
                addressEntity.getPincode() == null || addressEntity.getPincode().isEmpty() ||
                addressEntity.getUuid() == null || addressEntity.getUuid().isEmpty()) {
            throw new SaveAddressException("SAR-001", "No field can be empty.");
        }

        if (!addressEntity.getPincode().matches("^[1-9][0-9]{5}$")) {
            throw new SaveAddressException("SAR-002", "Invalid pincode.");
        }

        if (stateDao.getStateById(addressEntity.getStateId()) == null) {
            throw new AddressNotFoundException("ANF-002", "No state by this id.");
        }

        addressEntity = addressDao.createAddress(addressEntity);

        return addressEntity;
    }

    public List<AddressEntity> getAllAddress(String bearerToken) throws AuthorizationFailedException {

        validateAccessToken(bearerToken);

        //get the customerAuthToken details from customerDao
        CustomerAuthTokenEntity customerAuthTokenEntity = customerDao.getCustomerAuthToken(bearerToken);

        return customerAddressDao.getAddressForCustomerByUuid(customerAuthTokenEntity.getCustomer().getUuid());
    }

    private void validateAccessToken(String bearerToken) throws AuthorizationFailedException {
        final ZonedDateTime now = ZonedDateTime.now();

        //get the customerAuthToken details from customerDao
        CustomerAuthTokenEntity customerAuthTokenEntity = customerDao.getCustomerAuthToken(bearerToken);

        // Throw AuthorizationFailedException if the customer is not authorized
        if (customerAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
            // Throw AuthorizationFailedException if the customer is logged out
        } else if (customerAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
            // Throw AuthorizationFailedException if the customer session is expired
        } else if (now.isAfter(customerAuthTokenEntity.getExpiresAt())) {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }
    }
}
