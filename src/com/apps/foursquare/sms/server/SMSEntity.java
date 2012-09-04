package com.apps.foursquare.sms.server;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SMSEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	String mobilenumber;

	String status;
	
	String message;
	
	String sender;

    long date;

	public SMSEntity() {
		mobilenumber = null;
		status =null;
		message = null;
		sender = null;
	}
	
	public SMSEntity(String mobilenumber, String message, String status, long date) {
		this.mobilenumber = mobilenumber;
		this.message = message;
		this.status = status;
		this.date = date;
	}
	
	public String getMobilenumber() {
		return mobilenumber;
	}

	public void setMobilenumber(String mobilenumber) {
		this.mobilenumber = mobilenumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public long getId() {
		return id.longValue();
	}

    public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}
}
