package com.peachy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;

import com.peachy.dao.InventoryDao;
import com.peachy.entity.Inventory;
import com.peachy.entity.InvoiceItem;
import com.peachy.helper.Categories;
import com.peachy.interfaces.IInventory;

@Service
public class InventoryService implements IInventory {

	@Autowired
	InventoryDao inventoryDao;
	
	@Override
	public void create(Inventory inventory) {
		inventoryDao.create(inventory);
	}

	@Override
	public Inventory retrieve(String sku_num) {
		return inventoryDao.retrieve(sku_num);
	}

	@Override
	public void update(Inventory inventory) {
		inventoryDao.update(inventory);
	}

	@Override
	public void delete(Inventory inventory) {
		inventoryDao.delete(inventory);
	}

	public List<Inventory> listSaleItems() {
		return inventoryDao.listSaleItems();
	}

	public PagedListHolder<Inventory> searchInventory(String mySearch) {
		return new PagedListHolder<Inventory>(inventoryDao.searchProducts(mySearch));
	}
	
	public PagedListHolder<Inventory> getList(Categories categories) {
		PagedListHolder<Inventory> listHolder;
		
		if (categories.getCategory().length() == 0) {
			listHolder = new PagedListHolder<Inventory>(inventoryDao.listProducts());
		}else if (categories.getSubCategory().length() == 0) {
			listHolder = new PagedListHolder<Inventory>(inventoryDao.listProducts(categories.getCategory()));
		}else{
			listHolder = new PagedListHolder<Inventory>(inventoryDao.listProducts(categories.getCategory(), categories.getSubCategory()));
		}
		
		return listHolder;
	}

	public List<Inventory> getCategory() {
		return inventoryDao.getCategory();
	}

	public List<Inventory> getSubCategory(String category) {
		return inventoryDao.getSubCategory(category);
	}

	public void depleteInventory(List<InvoiceItem> invoiceItems) {
		inventoryDao.depleteInventory(invoiceItems);
		
	}
	
	public PagedListHolder<Inventory> listAllProducts() {
		return new PagedListHolder<Inventory>(inventoryDao.listAllProducts());
	}
	
	public PagedListHolder<Inventory> getReplenishList() {
		return new PagedListHolder<Inventory>(inventoryDao.getReplenishList());
	}
}
