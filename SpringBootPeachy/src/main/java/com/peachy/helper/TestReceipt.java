package com.peachy.helper;

import java.util.HashSet;
import java.util.Set;

import com.peachy.entity.InvoiceItem;

public class TestReceipt {
	private Set<InvoiceItem> items;
	private Long[] amount = {(long) 2,(long) 1,(long) 3,(long) 4,(long) 2,(long) 1,(long) 2,(long) 2, (long) 1,(long) 1};
	private String[] products = {"0099557009", "09556013", "09955020", "0099557041", "099555019", "099555022", "099556001", "099556002", "099556004", "099556006" };
	private double[] price = {400.00, 515.00, 600.00, 700.00, 350.00, 460.00, 400.00, 400.00, 375.00, 450.00};
	private double tax[] = {1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00};
	
	
	public TestReceipt() {
		items = new HashSet<InvoiceItem>();
		for (int i=0; i < 10; i++) {
			items.add(new InvoiceItem());
		}
		int idx = 0;
		for(InvoiceItem item : items) {
			item.setSku_num(products[idx]);
			item.setAmount(amount[idx]);
			item.setPrice(price[idx]);
			item.setTax(tax[idx]);
			idx++;
		}
	}

	public Double getHighestPrice() {
		Double price = 0.0;
		
		for(InvoiceItem item : items) {
			if (item.getSku_num().startsWith("CPN") ) continue;
			if (item.getPrice() > price) {
				price = item.getPrice();
			}
		}
		return price;
	}
	
	public Double getLowestPrice() {
		Double price = 999999999999.99;
		for(InvoiceItem item : items) {
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
		for(InvoiceItem item : items) {
			if (item.getSku_num().startsWith("CPN") ) continue;
			price += item.getPrice();
			count += 1.0;
		}
		if (count == 0) return 0.0;
		
		return price / count;
	}
	
	public Double getTotalPrice() {
		Double price = 0.0;
		for(InvoiceItem item : items) {
			if (item.getSku_num().startsWith("CPN") ) continue;
			price += item.getPrice();
		}
		return price;
	}
	
	public Long getCount() {
		
		return Long.valueOf(String.valueOf(items.size()));
	}
	
	public Long getTotalQty() {
		Long qty = 0L;
		
		for(InvoiceItem item : items) {
			if (item.getSku_num().startsWith("CPN") ) continue;
			qty += item.getAmount();
		}
		return qty;
	}
	
	public Long getItemQty(String sku) {
		Long qty = 0L;
		
		for(InvoiceItem item : items) {
			if (item.getSku_num().compareTo(sku) !=0) continue;
			qty += item.getAmount();
		}
		
		return qty;
	}
	
	public Double getItemPrice(String sku) {
		Double price = 0.0;
		
		for(InvoiceItem item : items) {
			if (item.getSku_num().compareTo(sku) !=0) continue;
			price = item.getPrice();
			break;
		}
		
		return price;
	}
	
	public Double getItemTax(String sku) {
		Double tax = 0.0;
		
		for(InvoiceItem item : items) {
			if (item.getSku_num().compareTo(sku) !=0) continue;
			tax = item.getTax();
			break;
		}
		
		return tax;
	}

	public Set<InvoiceItem> getItems() {
		return items;
	}

	public void setItems(Set<InvoiceItem> items) {
		this.items = items;
	}
	
	
}
