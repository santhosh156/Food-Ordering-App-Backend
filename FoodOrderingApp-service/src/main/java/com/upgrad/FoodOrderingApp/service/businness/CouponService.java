package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CouponDao;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CouponService {

    @Autowired
    private CouponDao couponDao;

    @Transactional
    public CouponEntity getCouponById(Long couponId) {
        return couponDao.getCouponById(couponId);
    }

}
