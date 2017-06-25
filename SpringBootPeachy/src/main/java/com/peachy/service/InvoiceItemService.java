package com.peachy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peachy.dao.InvoiceItemDao;
import com.peachy.entity.InvoiceItem;
import com.peachy.interfaces.IInvoiceItem;

@Service
public class InvoiceItemService implements IInvoiceItem {

	@Autowired
	InvoiceItemDao invoiceItemDao;
	
	
	@Override
	public void create(InvoiceItem item) {
		invoiceItemDao.create(item);
	}

	@Override
	public InvoiceItem retrieve(int item_id) {
		return invoiceItemDao.retrieve(item_id);
	}

	@Override
	public InvoiceItem retrieve(int invoice_num, String sku_num) {
		return invoiceItemDao.retrieve(invoice_num, sku_num);
	}

	@Override
	public boolean exists(int invoice_num, String sku_num) {
		return invoiceItemDao.exists(invoice_num, sku_num);
	}
	
	public boolean exists(int invoice_num) {
		return invoiceItemDao.exists(invoice_num);
	}

	@Override
	public void update(InvoiceItem item) {
		invoiceItemDao.update(item);
	}

	@Override
	public void delete(InvoiceItem item) {
		invoiceItemDao.delete(item);
	}
	
	public void merge(InvoiceItem item) {
		invoiceItemDao.merge(item);
	}
	
	public void addItem(InvoiceItem item) {
		if (exists(item.getInvoice_num(), item.getSku_num())) {
			InvoiceItem originalItem = retrieve(item.getInvoice_num(), item.getSku_num());
			originalItem.setAmount(originalItem.getAmount() + item.getAmount());
			originalItem.setOptions(originalItem.getOptions() + ";" + item.getOptions());
			merge(originalItem);
		}else{
			create(item);
		}
	}
	
	public long countCoupons(int user_id, String coupon) {
		return invoiceItemDao.countCoupons(user_id, coupon);
	}
	
	public boolean hasCoupons(int invoice_num) {
		return invoiceItemDao.hasCoupons(invoice_num);
	}

	public List<InvoiceItem> getLineItems(Integer invoice_num) {
		return invoiceItemDao.getLineItems(invoice_num);
	}
}
