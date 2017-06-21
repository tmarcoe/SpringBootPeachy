package com.peachy.interfaces;

import com.peachy.entity.Coupons;

public interface ICoupons {
	public void create(Coupons coupons);
	public Coupons retrieve(String coupon_id);
	public void update(Coupons coupons);
	public void delete(Coupons coupons);

}
