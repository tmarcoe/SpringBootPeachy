/**
 * 
 */
package com.peachy.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="invoice")
public class Invoice implements  Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer invoice_num;
	private int user_id;
	private double total;
	private double total_tax;
	private double shipping_cost;
	private double added_charges;
	private String payment_type;
	private boolean pod;
	private String payment_number;
	private boolean disable_coupons;
	private Date modified;
	private Date processed;
	private Date shipped;
	private Set<InvoiceItem> items = new HashSet<InvoiceItem>(0);
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="INVOICE_NUM" , unique = true, nullable = false)
	public Integer getInvoice_num() {
		return invoice_num;
	}
	public void setInvoice_num(Integer invoice_num) {
		this.invoice_num = invoice_num;
	}
	
	@Column(name="USER_ID")
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	
	@Column(name="TOTAL")
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	
	@Column(name="TOTAL_TAX")
	public double getTotal_tax() {
		return total_tax;
	}
	public void setTotal_tax(double total_tax) {
		this.total_tax = total_tax;
	}
	
	@Column(name="SHIPPING_COST")
	public double getShipping_cost() {
		return shipping_cost;
	}
	public void setShipping_cost(double shipping_cost) {
		this.shipping_cost = shipping_cost;
	}
	
	@Column(name="ADDED_CHARGES")
	public double getAdded_charges() {
		return added_charges;
	}
	public void setAdded_charges(double added_charges) {
		this.added_charges = added_charges;
	}
	
	@Column(name="PAYMENT_TYPE")
	public String getPayment_type() {
		return payment_type;
	}
	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}
	
	@Column(name="POD")
	public boolean isPod() {
		return pod;
	}
	public void setPod(boolean pod) {
		this.pod = pod;
	}
	
	@Column(name="PAYMENT_NUMBER")
	public String getPayment_number() {
		return payment_number;
	}
	public void setPayment_number(String payment_number) {
		this.payment_number = payment_number;
	}
	
	@Column(name="DISABLE_COUPONS")
	public boolean isDisable_coupons() {
		return disable_coupons;
	}
	public void setDisable_coupons(boolean disable_coupons) {
		this.disable_coupons = disable_coupons;
	}
	
	@Column(name="MODIFIED")
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}
	
	@Column(name="PROCESSED")
	public Date getProcessed() {
		return processed;
	}
	public void setProcessed(Date processed) {
		this.processed = processed;
	}
	
	@Column(name="SHIPPED")
	public Date getShipped() {
		return shipped;
	}
	public void setShipped(Date shipped) {
		this.shipped = shipped;
	}
	
	@OneToMany( fetch=FetchType.EAGER, orphanRemoval=true, mappedBy = "invoice")
	public Set<InvoiceItem> getItems() {
		return items;
	}
	public void setItems(Set<InvoiceItem> items) {
		this.items = items;
	}
	
	
}
