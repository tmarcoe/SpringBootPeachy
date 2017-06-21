package com.peachy.interfaces;

import com.peachy.entity.Inventory;

public interface IInventory {
	public void create(Inventory inventory);
	public Inventory retrieve(String sku_num);
	public void update(Inventory inventory);
	public void delete(Inventory inventory);
}
