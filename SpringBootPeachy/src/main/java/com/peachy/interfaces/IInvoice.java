package com.peachy.interfaces;

import com.peachy.entity.Invoice;

public interface IInvoice {
	public void create(Invoice header);
	public Invoice retrieve(int invoice_num);
	public void update(Invoice header);
	public void merge(Invoice header);
	public void delete(Invoice header);
}
