package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.StateDao;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class StateBusinessService {

    @Autowired
    private StateDao stateDao;

    @Transactional
    // A Method which takes the stateId as parameter for  getStateById endpoint
    public StateEntity getStateById (final Long stateId) {

        return stateDao.getStateById(stateId);
    }

    @Transactional
    public StateEntity getStateByUuid (final String stateUuid) {
        return stateDao.getStateByUuid(stateUuid);
    }
}
