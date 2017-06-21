package com.peachy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peachy.dao.PurchaseOrderDao;
import com.peachy.entity.PurchaseOrder;
import com.peachy.interfaces.IPurchaseOrder;

@Service
public class PurchaseOrderService implements IPurchaseOrder {

	@Autowired
	PurchaseOrderDao purchaseOrderDao;
	
	@Override
	public void create(PurchaseOrder purchaseOrder) {
		purchaseOrderDao.create(purchaseOrder);
	}

	@Override
	public PurchaseOrder retrieve(int orderId) {
		return purchaseOrderDao.retrieve(orderId);
	}

	@Override
	public void update(PurchaseOrder purchaseOrder) {
		purchaseOrderDao.update(purchaseOrder);
	}

	@Override
	public void delete(PurchaseOrder purchaseOrder) {
		purchaseOrderDao.delete(purchaseOrder);
	}

}
