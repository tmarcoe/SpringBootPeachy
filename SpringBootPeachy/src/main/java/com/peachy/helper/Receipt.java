package com.peachy.helper;

import java.util.Set;

import com.peachy.entity.InvoiceItem;

public class Receipt {
	private Set<InvoiceItem> invList;

	public void setInvList(Set <InvoiceItem> invList) {
		this.invList = invList;
	}
	
	public Double getHighestPrice() {
		Double price = 0.0;
		
		for(InvoiceItem item : invList) {
			if (item.getSku_num().startsWith("CPN") ) continue;
			if (item.getPrice() > price) {
				price = item.getPrice();
			}
		}
		return price;
	}
	
	public Double getLowestPrice() {
		Double price = 999999999999.99;
		for(InvoiceItem item : invList) {
			if (item.getSku_num().startsWith("CPN") ) continue;
			if (item.getPrice() < price) {
				price = item.getPrice();
			}
		}
		return price;
	}
	
	public Double getAvgPrice() {
		Double price = 0.0;
		Double count = 0.0;
		for(InvoiceItem item : invList) {
			if (item.getSku_num().startsWith("CPN") ) continue;
			price += item.getPrice();
			count += 1.0;
		}
		if (count == 0) return 0.0;
		
		return price / count;
	}
	
	public Double getTotalPrice() {
		Double price = 0.0;
		for(InvoiceItem item : invList) {
			if (item.getSku_num().startsWith("CPN") ) continue;
			price += item.getPrice();
		}
		return price;
	}
	
	public Long getCount() {
		
		return Long.valueOf(String.valueOf(invList.size()));
	}
	
	public Long getTotalQty() {
		Long qty = 0L;
		
		for(InvoiceItem item : invList) {
			if (item.getSku_num().startsWith("CPN") ) continue;
			qty += item.getAmount();
		}
		return qty;
	}
	
	public Long getItemQty(String sku) {
		Long qty = 0L;
		
		for(InvoiceItem item : invList) {
			if (item.getSku_num().compareTo(sku) !=0) continue;
			qty += item.getAmount();
		}
		
		return qty;
	}
	
	public Double getItemPrice(String sku) {
		Double price = 0.0;
		
		for(InvoiceItem item : invList) {
			if (item.getSku_num().compareTo(sku) !=0) continue;
			price = item.getPrice();
			break;
		}
		
		return price;
	}
	
	public Double getItemTax(String sku) {
		Double tax = 0.0;
		
		for(InvoiceItem item : invList) {
			if (item.getSku_num().compareTo(sku) !=0) continue;
			tax = item.getTax();
			break;
		}
		
		return tax;
	}
}
