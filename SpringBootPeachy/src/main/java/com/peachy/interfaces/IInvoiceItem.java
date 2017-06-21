package com.peachy.interfaces;

import com.peachy.entity.InvoiceItem;

public interface IInvoiceItem {
	public void create(InvoiceItem item);
	public InvoiceItem retrieve(int item_id);
	public InvoiceItem retrieve(int invoice_num, String sku_num);
	public boolean exists(int invoice_num, String sku_num);
	public void update(InvoiceItem item);
	public void delete(InvoiceItem item);
	

}
