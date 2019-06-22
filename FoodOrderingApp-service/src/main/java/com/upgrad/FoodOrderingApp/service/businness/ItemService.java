package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.ItemDao;
import com.upgrad.FoodOrderingApp.service.dao.OrderDao;
import com.upgrad.FoodOrderingApp.service.dao.OrderItemDao;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrdersEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ItemService {

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Transactional
    // A Method which takes the itemId as parameter for getItemEntityById
    public ItemEntity getItemEntityById(final Integer itemId){

        return itemDao.getItemById(itemId);
    }

    @Transactional
    // A Method which takes the item uuid as parameter for getItemEntityByUuid
    public ItemEntity getItemEntityById(final String itemUuid){

        return itemDao.getItemByUuid(itemUuid);
    }

    @Transactional
    public List<ItemEntity> getItemsByPopularity(RestaurantEntity restaurantEntity) {

        // List to store all items ordered in a restaurant
        List<ItemEntity> itemEntityList = new ArrayList<>();

        // Gets all the orders placed in the restaurant
        for (OrdersEntity orderEntity : orderDao.getOrdersByRestaurant(restaurantEntity.getId())) {
            // Gets items from each order placed in the restaurant
            for (OrderItemEntity orderItemEntity : orderItemDao.getItemsByOrder(orderEntity.getId())) {
                itemEntityList.add(orderItemEntity.getItem());
            }
        }


        // Maps each item with its respective order count
        Map<String, Integer> map = new HashMap<String, Integer>();
        Stream<Map.Entry<String,Integer>> sorted =
                map.entrySet().stream()
                        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));

        Map<String,Integer> topFive =
                map.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .limit(5)
                        .collect(Collectors.toMap(
                                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        /*for (ItemEntity itemEntity : itemEntityList) {
            Integer count = map.get(itemEntity.getUuid());
            map.put(itemEntity.getUuid(), (count == null) ? 1 : count + 1);
        }

        // Sorts the items based on the number of times ordered
        Map<String, Integer> treeMap = new TreeMap<String, Integer>(map);
        List<ItemEntity> sortedItemEntityList = new ArrayList<ItemEntity>();
        for (Map.Entry<String, Integer> entry : treeMap.entrySet()) {
            sortedItemEntityList.add(itemDao.getItemByUuid(entry.getKey()));
        }
        //Collections.reverse(sortedItemEntityList);*/

        List<ItemEntity> sortedItemEntityList = new ArrayList<ItemEntity>();
        for (Map.Entry<String, Integer> entry : topFive.entrySet()) {
            sortedItemEntityList.add(itemDao.getItemByUuid(entry.getKey()));
        }

        return sortedItemEntityList;
    }
}

