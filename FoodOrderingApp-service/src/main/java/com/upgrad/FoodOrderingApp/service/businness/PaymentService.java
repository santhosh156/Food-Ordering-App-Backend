package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.PaymentDao;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentDao paymentDao;

    @Transactional
    public List<PaymentEntity> getAllPaymentMethods() {
        return paymentDao.getAllPaymentMethods();
    }

    @Transactional
    public PaymentEntity getPaymentById(final Long paymentId) {
        return paymentDao.getPaymentById(paymentId);
    }

    @Transactional
    public PaymentEntity getPaymentByUuid(final String paymentUuid) {
        return paymentDao.getPaymentByUuid(paymentUuid);
    }
}
