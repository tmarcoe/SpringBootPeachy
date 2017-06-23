/**
 * 
 */
package com.peachy.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;


@Entity
public class UserProfile implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	int user_id;
	
	@Size(min = 2, max = 30)
	private String firstname;
	@Size(min = 2, max = 30)
	private String lastname;
	@NotNull
	private String maleFemale;
	@NotBlank
	private String address1;
	private String address2;
	@NotBlank
	private String city;
	private String region;
	private String postalCode;
	@Size(min = 3, max = 3)
	private String country;
	@NotBlank
	private String currency;
	private String homePhone;
	private String cellPhone;

	private String username;
	private String password;
	private String shippingInfo;
	private boolean monthlyMailing;

	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<Role> roles;
	private boolean enabled;
	private boolean dailySpecials;
	private Date dateAdded;
	@Transient
	private String roleString;

	public String getRoleString() {
		return roleString;
	}
	public void setRoleString(String roleString) {
		this.roleString = roleString;
	}
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "userProfile", cascade = CascadeType.ALL)
	private Employee employee;
	
	@Column(name="USER_ID")
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	
	@Column(name="FIRSTNAME")
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	@Column(name="LASTNAME")
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	@Column(name="MALE_FEMALE")
	public String getMaleFemale() {
		return maleFemale;
	}
	public void setMaleFemale(String maleFemale) {
		this.maleFemale = maleFemale;
	}
	
	@Column(name="ADDRESS1")
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	
	@Column(name="ADDRESS2")
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	
	@Column(name="CITY")
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	@Column(name="REGION")
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	
	@Column(name="POSTAL_CODE")
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	
	@Column(name="COUNTRY")
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	@Column(name="CURRENCY")
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	@Column(name="HOME_PHONE")
	public String getHomePhone() {
		return homePhone;
	}
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}
	
	@Column(name="CELL_PHONE")
	public String getCellPhone() {
		return cellPhone;
	}
	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
	
	@Column(name="USERNAME")
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Column(name="PASSWORD")
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(name="SHIPPING_INFO")
	public String getShippingInfo() {
		return shippingInfo;
	}
	public void setShippingInfo(String shippingInfo) {
		this.shippingInfo = shippingInfo;
	}
	
	@Column(name="MONTHYLY_MAILING")
	public boolean isMonthlyMailing() {
		return monthlyMailing;
	}
	public void setMonthlyMailing(boolean monthlyMailing) {
		this.monthlyMailing = monthlyMailing;
	}
	
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	@Column(name="ENABLED")
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Column(name="DAILY_SPECIALS")
	public boolean isDailySpecials() {
		return dailySpecials;
	}
	public void setDailySpecials(boolean dailySpecials) {
		this.dailySpecials = dailySpecials;
	}
	
	@Column(name="DATE_ADDED")
	public Date getDateAdded() {
		return dateAdded;
	}
	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}
	
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	

}
