package com.peachy.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.validator.constraints.NotBlank;

@Entity
public class TimeSheet implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private int entryId;
	
	private int userId;
	@NotBlank
	private String accountNum;
	private String name;
	private Date startPeriod;
	private double sunday;
	private double monday;
	private double tuesday;
	private double wednesday;
	private double thursday;
	private double friday;
	private double saturday;
	private Date entered;
	private Date submitted;
	private Date approved;
	private Date closed;
	private String comments;
	

	
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
	public String getAccountNum() {
		return accountNum;
	}
	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getStartPeriod() {
		return startPeriod;
	}
	public void setStartPeriod(Date startPeriod) {
		this.startPeriod = startPeriod;
	}
	public double getSunday() {
		return sunday;
	}
	public void setSunday(double sunday) {
		this.sunday = sunday;
	}
	public double getMonday() {
		return monday;
	}
	public void setMonday(double monday) {
		this.monday = monday;
	}
	public double getTuesday() {
		return tuesday;
	}
	public void setTuesday(double tuesday) {
		this.tuesday = tuesday;
	}
	public double getWednesday() {
		return wednesday;
	}
	public void setWednesday(double wednesday) {
		this.wednesday = wednesday;
	}
	public double getThursday() {
		return thursday;
	}
	public void setThursday(double thursday) {
		this.thursday = thursday;
	}
	public double getFriday() {
		return friday;
	}
	public void setFriday(double friday) {
		this.friday = friday;
	}
	public double getSaturday() {
		return saturday;
	}
	public void setSaturday(double saturday) {
		this.saturday = saturday;
	}
	public Date getEntered() {
		return entered;
	}
	public void setEntered(Date entered) {
		this.entered = entered;
	}
	public Date getSubmitted() {
		return submitted;
	}
	public void setSubmitted(Date submitted) {
		this.submitted = submitted;
	}
	public Date getApproved() {
		return approved;
	}
	public void setApproved(Date approved) {
		this.approved = approved;
	}
	public Date getClosed() {
		return closed;
	}
	public void setClosed(Date closed) {
		this.closed = closed;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	

}
