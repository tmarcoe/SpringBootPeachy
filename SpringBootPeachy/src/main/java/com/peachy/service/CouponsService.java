package com.peachy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
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
	
	public Coupons retrieveByName(String name) {
		return couponsDao.retrieveByName(name);
	}
	
	public PagedListHolder<Coupons> retrieveList() {
		return new PagedListHolder<Coupons>(couponsDao.retrieveList());
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
