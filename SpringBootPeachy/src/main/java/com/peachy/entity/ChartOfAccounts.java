/**
 * 
 */
package com.peachy.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;


@Entity
@Table(name="ChartOfAccounts")
public class ChartOfAccounts implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@NotBlank
	private String accountNum;
	@NotBlank
	private String accountName;
	private double accountBalance;
	private boolean debitAccount;
	private String Description;
	
	
	public String getAccountNum() {
		return accountNum;
	}
	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public double getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(double accountBalance) {
		this.accountBalance = accountBalance;
	}
	public boolean isDebitAccount() {
		return debitAccount;
	}
	public void setDebitAccount(boolean debitAccount) {
		this.debitAccount = debitAccount;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	
}
