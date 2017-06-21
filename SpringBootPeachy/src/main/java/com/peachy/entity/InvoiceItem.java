package com.peachy.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;

@Entity
public class InvoiceItem implements  Serializable {
	private static final long serialVersionUID = 1L;
	
	private int item_id;
	private int invoice_num;
	private String sku_num;
	private Invoice invoice;
	private String product_name;
	@Min(1)
	private long amount;
	private double price;
	private double tax;
	private long amt_in_stock;
	private double weight;
	private boolean disable_coupons;
	private String options;

	
	
	public InvoiceItem() {
	}
	
	public InvoiceItem(int item_id, String sku_num) {
		this.item_id = item_id;
		this.sku_num = sku_num;
	}

	public InvoiceItem( Invoice invoice, int invoice_num) {
		this.invoice_num = invoice_num;
		this.invoice = invoice;
	}
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ITEM_ID")
	public int getItem_id() {
		return item_id;
	}
	public void setItem_id(int item_id) {
		this.item_id = item_id;
	}
	
	@Column(name = "INVOICE_NUM")
	public int getInvoice_num() {
		return invoice_num;
	}
	public void setInvoice_num(int invoice_num) {
		this.invoice_num = invoice_num;
	}

	@Column(name = "SKU_NUM")
	public String getSku_num() {
		return sku_num;
	}
	public void setSku_num(String sku_num) {
		this.sku_num = sku_num;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="INVOICE_NUM", referencedColumnName="INVOICE_NUM", nullable = false, insertable=false, updatable=false)
	public Invoice getInvoice() {
		return invoice;
	}
	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	@Column(name = "AMOUNT")
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}

	@Column(name = "PRICE")
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}

	@Column(name = "TAX")
	public double getTax() {
		return tax;
	}
	public void setTax(double tax) {
		this.tax = tax;
	}

	@Column(name = "AMT_IN_STOCK")
	public long getAmt_in_stock() {
		return amt_in_stock;
	}
	public void setAmt_in_stock(long amt_in_stock) {
		this.amt_in_stock = amt_in_stock;
	}

	@Column(name = "WEIGHT")
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}

	@Column(name = "DISABLE_COUPONS")
	public boolean isDisable_coupons() {
		return disable_coupons;
	}
	public void setDisable_coupons(boolean disable_coupons) {
		this.disable_coupons = disable_coupons;
	}

	@Column(name = "OPTIONS")
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
	}

	

}
