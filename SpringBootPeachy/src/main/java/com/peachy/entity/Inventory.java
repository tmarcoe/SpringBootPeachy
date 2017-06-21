/**
 * 
 */
package com.peachy.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;



@Entity
public class Inventory implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@NotBlank
	private String sku_num;
	@Size(min=2, max=255)
	private String product_name;
	@NotBlank
	private String category;
	@NotBlank
	private String subcategory;
	@NotNull
	private int amt_in_stock;
	@NotNull
	private int amt_committed;
	@NotNull
	private int min_quantity;
	@NotNull
	private double sale_price;
	@NotNull
	private double discount_price;
	@NotNull
	private double tax_amt;

	private double weight;
	private boolean on_sale;
	private String image;
	private String extra_info_scheme;
	@Size(min=10, max=1000)
	private String description;
	
	
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getExtra_info_scheme() {
		return extra_info_scheme;
	}
	public void setExtra_info_scheme(String extraInfoScheme) {
		this.extra_info_scheme = extraInfoScheme;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSku_num() {
		return sku_num;
	}
	public void setSku_num(String skuNum) {
		this.sku_num = skuNum;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String productName) {
		this.product_name = productName;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getSubcategory() {
		return subcategory;
	}
	public void setSubcategory(String subCategory) {
		this.subcategory = subCategory;
	}
	public int getAmt_in_stock() {
		return amt_in_stock;
	}
	public void setAmt_in_stock(int amtInStock) {
		this.amt_in_stock = amtInStock;
	}
	public int getAmt_committed() {
		return amt_committed;
	}
	public void setAmt_committed(int amtCommitted) {
		this.amt_committed = amtCommitted;
	}
	public int getMin_quantity() {
		return min_quantity;
	}
	public void setMin_quantity(int minQuantity) {
		this.min_quantity = minQuantity;
	}
	public double getSale_price() {
		return sale_price;
	}
	public void setSale_price(double salePrice) {
		this.sale_price = salePrice;
	}
	public double getDiscount_price() {
		return discount_price;
	}
	public void setDiscount_price(double discountPrice) {
		this.discount_price = discountPrice;
	}
	public double getTax_amt() {
		return tax_amt;
	}
	public void setTax_amt(double taxAmt) {
		this.tax_amt = taxAmt;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public boolean isOn_sale() {
		return on_sale;
	}
	public void setOn_sale(boolean onSale) {
		this.on_sale = onSale;
	}
	
	
		
}
