package com.peachy.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peachy.dao.PurchaseOrderDao;
import com.peachy.entity.PurchaseOrder;
import com.peachy.interfaces.IPurchaseOrder;

@Service
public class PurchaseOrderService implements IPurchaseOrder {

	@Autowired
	PurchaseOrderDao purchaseOrderDao;
	
	@Autowired
	InvoiceService invoiceService;
	

	
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
	public List<Double> getCOGSReport(int year) {
		Double price = 0.0;
		int count = 0;
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -6);
		Date history = cal.getTime();
		List<PurchaseOrder> po = purchaseOrderDao.getPurchaseOrderList(history);
		for (PurchaseOrder item: po) {
			price += item.getPrice();
			count += item.getQty();
		}
		BigDecimal avgCost =  getAvgCost(BigDecimal.valueOf(price), count);
		List<Double> salesList = new ArrayList<Double>();
		for (int i = 1; i <= 12; i++) {
			BigDecimal total = invoiceService.getMonthlyTotal(i, year);
			if (total == null) {
				total = BigDecimal.valueOf(0.0);
			}
			salesList.add( invoiceService.getCountByMonth(i, year) - (avgCost.doubleValue() * total.doubleValue()));
		}
				
		return salesList;
	}
	private BigDecimal getAvgCost(BigDecimal cost, int amount) {
		
		if (amount > 0) {
			return cost.divide(BigDecimal.valueOf(Long.valueOf(String.valueOf(amount))), 2, RoundingMode.CEILING);
		}else{
			return BigDecimal.valueOf(0);
		}
	}
	public BigDecimal getMonthlyTotal(int month, int year) {
		
		return invoiceService.getMonthlyTotal(month, year);
	}


}
