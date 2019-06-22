package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CategoryBusinessService {

    @Autowired
    private CategoryDao categoryDao;

    // A Method which takes the categoryId as parameter for  getCategoryEntityById endpoint
    public CategoryEntity getCategoryEntityById(final Integer categoryId){
        return  categoryDao.getCategoryById(categoryId);
    }

    // A Method which takes the categoryUUId as parameter for  getCategoryEntityByUUId endpoint
    public CategoryEntity getCategoryEntityByUuid(final String categoryUUId){
        return  categoryDao.getCategoryByUUId(categoryUUId);
    }

    // A Method which is for  getAllCategories endpoint
    public List<CategoryEntity> getAllCategories(){
        return  categoryDao.getAllCategories();
    }

}

