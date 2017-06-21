package com.peachy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peachy.dao.PaymentRegisterDao;
import com.peachy.entity.PaymentRegister;
import com.peachy.interfaces.IPaymentRegister;

@Service
public class PaymentRegisterService implements IPaymentRegister {

	@Autowired
	PaymentRegisterDao paymentRegisterDao;
	
	@Override
	public void create(PaymentRegister paymentRegister) {
		paymentRegisterDao.create(paymentRegister);
	}

	@Override
	public PaymentRegister retrieve(int entryId) {
		return paymentRegisterDao.retrieve(entryId);
	}

	@Override
	public void update(PaymentRegister paymentRegister) {
		paymentRegisterDao.update(paymentRegister);
	}

	@Override
	public void delete(PaymentRegister paymentRegister) {
		paymentRegisterDao.delete(paymentRegister);
	}

}
