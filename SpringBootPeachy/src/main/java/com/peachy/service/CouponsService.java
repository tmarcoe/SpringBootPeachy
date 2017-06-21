package com.peachy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peachy.dao.CouponsDao;
import com.peachy.entity.Coupons;
import com.peachy.interfaces.ICoupons;

@Service
public class CouponsService implements ICoupons {

	@Autowired
	CouponsDao couponsDao;
	
	@Override
	public void create(Coupons coupons) {
		couponsDao.create(coupons);
	}

	@Override
	public Coupons retrieve(String coupon_id) {
		return couponsDao.retrieve(coupon_id);
	}

	@Override
	public void update(Coupons coupons) {
		couponsDao.update(coupons);
	}

	@Override
	public void delete(Coupons coupons) {
		couponsDao.delete(coupons);
	}

}
