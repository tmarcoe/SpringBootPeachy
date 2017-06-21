package com.peachy.exceptions;

public class SessionTimedOutException extends Exception{
	private static final long serialVersionUID = -1283372754602194792L;
	String msg;
	
	public SessionTimedOutException(String msg) {
		super();
		this.msg = msg;
	}
	
	   @Override
	   public String toString() { 
	      return (msg);
	   }
}
