package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class AddressBusinessService {

    @Autowired
    private AddressDao addressDao;

    @Transactional
    public AddressEntity getAddressById(final String addressId) {
        return addressDao.getAddressByUuid(addressId);
    }
}
