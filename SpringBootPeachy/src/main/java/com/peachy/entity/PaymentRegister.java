package com.peachy.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PaymentRegister implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private int entryId;
	
	private int userId;
	private Date startPeriod;
	private String paymentMethod;
	private String accountNum;
	private String routingNum;
	private double grossWage;
	private double netWage;
	private double federalTx;
	private double stateTx;
	private double federalUnEm;
	private double stateUnEm;
	private double medical;
	private double ssiTx;
	private double retirement;
	private double garnishment;
	private double other;
	private String otherExpl;
	
	public int getEntryId() {
		return entryId;
	}
	public void setEntryId(int entryId) {
		this.entryId = entryId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Date getStartPeriod() {
		return startPeriod;
	}
	public void setStartPeriod(Date startPeriod) {
		this.startPeriod = startPeriod;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public String getAccountNum() {
		return accountNum;
	}
	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}
	public String getRoutingNum() {
		return routingNum;
	}
	public void setRoutingNum(String routingNum) {
		this.routingNum = routingNum;
	}
	public double getGrossWage() {
		return grossWage;
	}
	public void setGrossWage(double grossWage) {
		this.grossWage = grossWage;
	}
	public double getNetWage() {
		return netWage;
	}
	public void setNetWage(double netWage) {
		this.netWage = netWage;
	}
	public double getFederalTx() {
		return federalTx;
	}
	public void setFederalTx(double federalTx) {
		this.federalTx = federalTx;
	}
	public double getStateTx() {
		return stateTx;
	}
	public void setStateTx(double stateTx) {
		this.stateTx = stateTx;
	}
	public double getFederalUnEm() {
		return federalUnEm;
	}
	public void setFederalUnEm(double federalUnEm) {
		this.federalUnEm = federalUnEm;
	}
	public double getStateUnEm() {
		return stateUnEm;
	}
	public void setStateUnEm(double stateUnEm) {
		this.stateUnEm = stateUnEm;
	}
	public double getMedical() {
		return medical;
	}
	public void setMedical(double medical) {
		this.medical = medical;
	}
	public double getSsiTx() {
		return ssiTx;
	}
	public void setSsiTx(double ssiTx) {
		this.ssiTx = ssiTx;
	}
	public double getRetirement() {
		return retirement;
	}
	public void setRetirement(double retirement) {
		this.retirement = retirement;
	}
	public double getGarnishment() {
		return garnishment;
	}
	public void setGarnishment(double garnishment) {
		this.garnishment = garnishment;
	}
	public double getOther() {
		return other;
	}
	public void setOther(double other) {
		this.other = other;
	}
	public String getOtherExpl() {
		return otherExpl;
	}
	public void setOtherExpl(String otherExpl) {
		this.otherExpl = otherExpl;
	}	

}
