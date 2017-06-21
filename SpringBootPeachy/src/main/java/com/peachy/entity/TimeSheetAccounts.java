package com.peachy.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TimeSheetAccounts implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String AccountNum;
	private String name;
	private double hoursBilled;
	private double maxHours;
	
	public String getAccountNum() {
		return AccountNum;
	}
	public void setAccountNum(String accountNum) {
		AccountNum = accountNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getHoursBilled() {
		return hoursBilled;
	}
	public void setHoursBilled(double hoursBilled) {
		this.hoursBilled = hoursBilled;
	}
	public double getMaxHours() {
		return maxHours;
	}
	public void setMaxHours(double maxHours) {
		this.maxHours = maxHours;
	}

}
