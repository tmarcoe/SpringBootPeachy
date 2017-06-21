package com.peachy.interfaces;

import com.peachy.entity.PurchaseOrder;

public interface IPurchaseOrder {
	public void create(PurchaseOrder purchaseOrder);
	public PurchaseOrder retrieve(int orderId);
	public void update(PurchaseOrder purchaseOrder);
	public void delete(PurchaseOrder purchaseOrder);
}
