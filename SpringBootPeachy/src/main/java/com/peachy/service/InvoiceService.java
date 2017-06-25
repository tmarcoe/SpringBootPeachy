package com.peachy.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;

import com.peachy.dao.InvoiceDao;
import com.peachy.dao.InvoiceItemDao;
import com.peachy.entity.Invoice;
import com.peachy.entity.InvoiceItem;
import com.peachy.interfaces.IInvoice;

@Service
public class InvoiceService implements IInvoice {
	
	@Autowired
	InvoiceDao invoiceDao;
	
	@Autowired 
	InvoiceItemDao invoiceItemDao;

	@Override
	public void create(Invoice header) {
		invoiceDao.create(header);
	}

	@Override
	public Invoice retrieve(int invoice_num) {
		return invoiceDao.retrieve(invoice_num);
	}

	@Override
	public void update(Invoice header) {
		invoiceDao.update(header);
	}

	@Override
	public void delete(Invoice header) {
		invoiceDao.delete(header);
	}

	@Override
	public void merge(Invoice header) {
		invoiceDao.merge(header);
	}

	public Invoice getOpenOrder(int user_id) {
		return invoiceDao.getOpenOrder(user_id);
	}

	public void removeItem(int invoice_num, String sku_num) {
		invoiceDao.removeItem(invoice_num, sku_num);
	}
	public Invoice totalInvoice(Invoice invoice) {
		invoice.setTotal(0);
		invoice.setTotal_tax(0);
		for(InvoiceItem item: invoice.getItems()) {
			invoice.setTotal(invoice.getTotal() + (item.getPrice() * item.getAmount()));
			invoice.setTotal_tax(invoice.getTotal_tax() + (item.getTax() * item.getAmount()));
		}
		
		return invoice;
	}
	
	public void purgeCoupons(int invoice_num) {
		Invoice invoice = retrieve(invoice_num);
		if (invoiceDao.purgeCoupons(invoice_num) == 0) {
			delete(invoice);
		}else{
			merge(invoice);
		}
	}

	public void deleteInvoice(int invoice_num) {
		invoiceDao.deleteInvoice(invoice_num);
	}
	
	public PagedListHolder<Invoice> getHistory(int user_id) {
		return new PagedListHolder<Invoice>(invoiceDao.getHistory(user_id));
	}
	
	public PagedListHolder<Invoice> getProcessedInvoices() {
		return new PagedListHolder<Invoice>(invoiceDao.getProcessedInvoices());
	}
	public List<Invoice> getProcessedInvoicesRawList() {
		return invoiceDao.getProcessedInvoices();
	}
	public List<Double> getSalesData(int year) {
		List<Double> totals = new ArrayList<Double>();
		for (int i=1;  i <= 12; i++) {
			totals.add(invoiceDao.getCountByMonth(i, year));
		}
		
		return totals;
	}
	public List<BigInteger> getCustomerCounts(int year) {
		List<BigInteger> totals = new ArrayList<BigInteger>();
		for (int i=1; i <= 12; i++) {
			totals.add(invoiceDao.getCustomerCounts(i, year));
		}
		
		return totals;
	}

	public BigDecimal getMonthlyTotal(int month, int year) {
		return invoiceDao.getMonthlyTotal(month, year);
	}
	
	public double getCountByMonth(int month, int year) {
		return invoiceDao.getCountByMonth(month, year);
	}
}
