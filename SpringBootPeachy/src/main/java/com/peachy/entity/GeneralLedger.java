/**
 * 
 */
package com.peachy.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class GeneralLedger implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private long entryNum;
	private String accountNum;
	private int userID;
	private Date entryDate;
	private String description;
	private double debitAmt;
	private double creditAmt;
	
	public GeneralLedger(String accountNum, int userID, Date entryDate,
			String description, double debitAmt, double creditAmt) {
		this.accountNum = accountNum;
		this.userID = userID;
		this.entryDate = entryDate;
		this.description = description;
		this.debitAmt = debitAmt;
		this.creditAmt = creditAmt;
	}
	
	public GeneralLedger() {
	}

	public long getEntryNum() {
		return entryNum;
	}
	public void setEntryNum(long entryNum) {
		this.entryNum = entryNum;
	}
	public String getAccountNum() {
		return accountNum;
	}
	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public Date getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getDebitAmt() {
		return debitAmt;
	}
	public void setDebitAmt(double debitAmt) {
		this.debitAmt = debitAmt;
	}
	public double getCreditAmt() {
		return creditAmt;
	}
	public void setCreditAmt(double creditAmt) {
		this.creditAmt = creditAmt;
	}
}
