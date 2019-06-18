package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;

    @RequestMapping(method = RequestMethod.POST, path = "/address", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveAddressResponse> saveAddress(
            @RequestHeader(value = "authorization", required = true) String authorization,
            @RequestBody(required = false) final SaveAddressRequest saveAddressRequest) throws AuthorizationFailedException, SaveAddressException, AddressNotFoundException {
        // Authentication Token
        String[] bearerToken = authorization.split("Bearer ");

        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setCity(saveAddressRequest.getCity());
        addressEntity.setFlatBuildingNumber(saveAddressRequest.getFlatBuildingName());
        addressEntity.setLocality(saveAddressRequest.getLocality());
        addressEntity.setPincode(saveAddressRequest.getPincode());
        addressEntity.setStateId(addressService.getStateByUUID(saveAddressRequest.getStateUuid()).getId());
        addressEntity.setUuid(UUID.randomUUID().toString());
        addressEntity.setActive(1L);

        // save the address
        AddressEntity savedAddressEntity = addressService.saveAddress(addressEntity, bearerToken[1]);
        SaveAddressResponse saveAddressResponse = new SaveAddressResponse().id(savedAddressEntity.getUuid()).status("ADDRESS SUCCESSFULLY REGISTERED");
        return new ResponseEntity<>(saveAddressResponse, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/address/customer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AddressListResponse> getAllPermanentAddress(String authorization) throws AuthorizationFailedException {
        // Authentication Token
        String[] bearerToken = authorization.split("Bearer ");

        List<AddressEntity> addressEntityList = addressService.getAllAddress(bearerToken[1]);

        AddressListResponse addressListResponse = new AddressListResponse();
        for (AddressEntity ae : addressEntityList) {
            StateEntity se = addressService.getStateById(ae.getStateId());

            AddressListState addressListState = new AddressListState();
            addressListState.setStateName(se.getStateName());
            AddressList addressList = new AddressList().city(ae.getCity()).flatBuildingName(ae.getFlatBuildingNumber())
                    .locality(ae.getLocality()).pincode(ae.getPincode()).state(addressListState);
            addressListResponse.addAddressesItem(addressList);
        }

        return new ResponseEntity<>(addressListResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/address/{address_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<DeleteAddressResponse> deleteAddress(String authorization, @PathVariable("address_id") UUID addressId) throws AuthorizationFailedException, SaveAddressException, AddressNotFoundException {
        // Authentication Token
        String[] bearerToken = authorization.split("Bearer ");

        addressService.deleteAddress(addressId, bearerToken[1]);

        DeleteAddressResponse deleteAddressResponse = new DeleteAddressResponse().id(addressId).status("ADDRESS DELETED SUCCESSFULLY");
        return new ResponseEntity<>(deleteAddressResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/states", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<StatesListResponse> getAll(String authorization) throws AuthorizationFailedException {
        // Authentication Token
        String[] bearerToken = authorization.split("Bearer ");

        List<StateEntity> stateEntityList = addressService.getAllStates(bearerToken[1]);
        StatesListResponse stateListResponse = new StatesListResponse();
        for (StateEntity se : stateEntityList) {
            StatesList state = new StatesList();
            state.setStateName(se.getStateName());
            state.setId(se.getUuid());
            stateListResponse.addStatesItem(state);
        }

        return new ResponseEntity<>(stateListResponse, HttpStatus.OK);
    }
}
