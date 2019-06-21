package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.ItemDao;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ItemBusinessService {
    @Autowired
    private ItemDao itemDao;

    @Transactional
    // A Method which takes the itemId as parameter for  getItemEntityById endpoint
    public ItemEntity getItemEntityById(final Integer itemId){

        return itemDao.getItemById(itemId);
    }
}

