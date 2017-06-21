package com.peachy.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
public class Employee implements Serializable {
	private static final long serialVersionUID = 1L;
	

	private int user_id;
	private Date startDate;
	private Date endDate;
	private String position;
	private String taxId;
	private double hourlyRate;
	private double fTaxPrcnt;
	private double sTaxPrcnt;
	private double fUnPrcnt;
	private double sUnPrcnt;
	private double medPrcnt;
	private double ssiPrcnt;
	private double retirePrcnt;
	private double garnishment;
	private double other;
	private String otherExpl;
	private double wagesYtd;
	private double fTaxYtd;
	private double sTaxYtd;
	private double fUnYtd;
	private double sUnYtd;
	private double medYtd;
	private double ssiYtd;
	private double retireYtd;
	private double garnishmentYtd;
	private double otherYtd;
	private String payMethod;
	private String accountNum;
	private String routingNum;
	private boolean fullTime;
	private boolean salary;
	private boolean exempt;
	private UserProfile userProfile;
	
	@Id 
	@Column(name="USER_ID")
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	
	@Column(name="START_DATE")
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@Column(name="END_DATE")
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name="POSITION")
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	
	@Column(name="TAX_ID")
	public String getTaxId() {
		return taxId;
	}
	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	@Column(name="HOURLY_RATE")
	public double getHourlyRate() {
		return hourlyRate;
	}
	public void setHourlyRate(double hourlyRate) {
		this.hourlyRate = hourlyRate;
	}
	
	@Column(name="F_TAX_PRCNT")
	public double getfTaxPrcnt() {
		return fTaxPrcnt;
	}
	public void setfTaxPrcnt(double fTaxPrcnt) {
		this.fTaxPrcnt = fTaxPrcnt;
	}
	
	@Column(name="S_TAX_PRCNT")
	public double getsTaxPrcnt() {
		return sTaxPrcnt;
	}
	public void setsTaxPrcnt(double sTaxPrcnt) {
		this.sTaxPrcnt = sTaxPrcnt;
	}
	
	@Column(name="F_UN_PRCNT")
	public double getfUnPrcnt() {
		return fUnPrcnt;
	}
	public void setfUnPrcnt(double fUnPrcnt) {
		this.fUnPrcnt = fUnPrcnt;
	}
	
	@Column(name="S_UN_PRCNT")
	public double getsUnPrcnt() {
		return sUnPrcnt;
	}
	public void setsUnPrcnt(double sUnPrcnt) {
		this.sUnPrcnt = sUnPrcnt;
	}
	
	@Column(name="MED_PRCNT")
	public double getMedPrcnt() {
		return medPrcnt;
	}
	public void setMedPrcnt(double medPrcnt) {
		this.medPrcnt = medPrcnt;
	}
	
	@Column(name="SSI_PRCNT")
	public double getSsiPrcnt() {
		return ssiPrcnt;
	}
	public void setSsiPrcnt(double ssiPrcnt) {
		this.ssiPrcnt = ssiPrcnt;
	}
	
	@Column(name="RETIRE_PRCNT")
	public double getRetirePrcnt() {
		return retirePrcnt;
	}
	public void setRetirePrcnt(double retirePrcnt) {
		this.retirePrcnt = retirePrcnt;
	}
	
	@Column(name="GARNISHMENT")
	public double getGarnishment() {
		return garnishment;
	}
	public void setGarnishment(double garnishment) {
		this.garnishment = garnishment;
	}
	
	@Column(name="OTHER")
	public double getOther() {
		return other;
	}
	public void setOther(double other) {
		this.other = other;
	}
	
	@Column(name="OTHER_EXPL")
	public String getOtherExpl() {
		return otherExpl;
	}
	public void setOtherExpl(String otherExpl) {
		this.otherExpl = otherExpl;
	}
	
	@Column(name="WAGES_YTD")
	public double getWagesYtd() {
		return wagesYtd;
	}
	public void setWagesYtd(double wagesYtd) {
		this.wagesYtd = wagesYtd;
	}
	
	@Column(name="F_TAX_YTD")
	public double getfTaxYtd() {
		return fTaxYtd;
	}
	public void setfTaxYtd(double fTaxYtd) {
		this.fTaxYtd = fTaxYtd;
	}
	
	@Column(name="S_TAX_YTD")
	public double getsTaxYtd() {
		return sTaxYtd;
	}
	public void setsTaxYtd(double sTaxYtd) {
		this.sTaxYtd = sTaxYtd;
	}
	
	@Column(name="F_UN_YTD")
	public double getfUnYtd() {
		return fUnYtd;
	}
	public void setfUnYtd(double fUnYtd) {
		this.fUnYtd = fUnYtd;
	}
	
	@Column(name="S_UN_YTD")
	public double getsUnYtd() {
		return sUnYtd;
	}
	public void setsUnYtd(double sUnYtd) {
		this.sUnYtd = sUnYtd;
	}
	
	@Column(name="MED_YTD")
	public double getMedYtd() {
		return medYtd;
	}
	public void setMedYtd(double medYtd) {
		this.medYtd = medYtd;
	}
	
	@Column(name="SSI_YTD")
	public double getSsiYtd() {
		return ssiYtd;
	}
	public void setSsiYtd(double ssiYtd) {
		this.ssiYtd = ssiYtd;
	}
	
	@Column(name="RETIRE_YTD")
	public double getRetireYtd() {
		return retireYtd;
	}
	public void setRetireYtd(double retireYtd) {
		this.retireYtd = retireYtd;
	}
	
	@Column(name="GARNISHMENT_YTD")
	public double getGarnishmentYtd() {
		return garnishmentYtd;
	}
	public void setGarnishmentYtd(double garnishmentYtd) {
		this.garnishmentYtd = garnishmentYtd;
	}
	
	@Column(name="OTHER_YTD")
	public double getOtherYtd() {
		return otherYtd;
	}
	public void setOtherYtd(double otherYtd) {
		this.otherYtd = otherYtd;
	}
	
	@Column(name="PAY_METHOD")
	public String getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	
	@Column(name="ACCOUNT_NUM")
	public String getAccountNum() {
		return accountNum;
	}
	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}
	
	@Column(name="ROUTING_NUM")
	public String getRoutingNum() {
		return routingNum;
	}
	public void setRoutingNum(String routingNum) {
		this.routingNum = routingNum;
	}
	
	@Column(name="FULL_TIME")
	public boolean isFullTime() {
		return fullTime;
	}
	public void setFullTime(boolean fullTime) {
		this.fullTime = fullTime;
	}
	
	@Column(name="SALARY")
	public boolean isSalary() {
		return salary;
	}
	public void setSalary(boolean salary) {
		this.salary = salary;
	}
	
	@Column(name="EXEMPT")
	public boolean isExempt() {
		return exempt;
	}
	public void setExempt(boolean exempt) {
		this.exempt = exempt;
	}
	
	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public UserProfile getUserProfile() {
		return userProfile;
	}
	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}
	
		
}
