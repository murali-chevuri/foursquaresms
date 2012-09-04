package com.apps.foursquare.sms.server;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SubscriberEntity {
	public SubscriberEntity(String mobilenumber, String status,
			String authToken, String code) {
		super();
		this.mobilenumber = mobilenumber;
		this.status = status;
		this.authToken = authToken;
		this.code = code;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	String mobilenumber;

	String status;
	
	String authToken;
	
	String code;

	
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

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public long getId() {
		return id.longValue();
	}
	
}
