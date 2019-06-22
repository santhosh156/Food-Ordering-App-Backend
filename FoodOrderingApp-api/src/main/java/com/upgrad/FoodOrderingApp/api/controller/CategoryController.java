package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CategoryDetailsResponse;
import com.upgrad.FoodOrderingApp.api.model.CategoryListResponse;
import com.upgrad.FoodOrderingApp.api.model.ItemList;
import com.upgrad.FoodOrderingApp.service.businness.CategoryBusinessService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/")
public class CategoryController {

    @Autowired
    private CategoryBusinessService categoryBusinessService;

    /**
     *
     * @return All categories stored in database
     */
    @RequestMapping(method = RequestMethod.GET, path = "/category", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getAllCategories() {

        // Getting the list of all categories with help of category business service
        final List<CategoryEntity> allCategories = categoryBusinessService.getAllCategories();

        // Adding the list of categories to categoriesList
        List<CategoryListResponse> categoriesList = new ArrayList<CategoryListResponse>();
        for (CategoryEntity n: allCategories) {
            CategoryListResponse categoryDetail = new CategoryListResponse();
            categoryDetail.setCategoryName(n.getCategoryName());
            categoryDetail.setId(UUID.fromString(n.getUuid()));
            categoriesList.add(categoryDetail);
        }

        // return response entity with CategoriesList(details) and Http status
        return new ResponseEntity<>(categoriesList, HttpStatus.OK);
    }

    /**
     *
     * @param category_id
     * @return Category with full details like items based on given category id
     * @throws CategoryNotFoundException - when category id field is empty
     */
    @RequestMapping(method = RequestMethod.GET, path = "/category/{category_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getCategoryById(@PathVariable String category_id) throws CategoryNotFoundException{

        // Throw exception if path variable(category_id) is empty
        if(category_id == null || category_id.isEmpty() || category_id.equalsIgnoreCase("\"\"")){
            throw new CategoryNotFoundException("CNF-001", "Category id field should not be empty");
        }

        // Getting category which matched with given category_id with help of category business service
        final CategoryEntity category = categoryBusinessService.getCategoryEntityByUuid(category_id);

        // Throw exception if given category_id not matched with any category in DB
        if(category == null){
            throw new CategoryNotFoundException("CNF-002", "No category by this id");
        }

        // Adding the values into categoryDetails
        CategoryDetailsResponse categoryDetails = new CategoryDetailsResponse();
        categoryDetails.setCategoryName(category.getCategoryName());
        categoryDetails.setId(UUID.fromString(category.getUuid()));
        List<ItemList> itemLists = new ArrayList();
        for (ItemEntity itemEntity :category.getItemEntities()) {
            ItemList itemDetail = new ItemList();
            itemDetail.setId(UUID.fromString(itemEntity.getUuid()));
            itemDetail.setItemName(itemEntity.getItemName());
            itemDetail.setPrice(itemEntity.getPrice());
            itemDetail.setItemType(itemEntity.getType());
            itemLists.add(itemDetail);
        }

        // Adding the values into categoryDetails
        categoryDetails.setItemList(itemLists);

        // return response entity with categoryDetails(details) and Http status
        return new ResponseEntity<>(categoryDetails, HttpStatus.OK);
    }

}
