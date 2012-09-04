package com.apps.foursquare.sms.server;

import java.io.Serializable;

public class FSVenues implements Serializable{
	public FSVenues(String name, String latitude, String longitude) {
		super();
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	String name;
	String latitude;
	String longitude;
}
