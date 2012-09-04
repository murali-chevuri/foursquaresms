package com.apps.foursquare.sms.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	String getSubscriberStatus(String mobilenumber) throws IllegalArgumentException;
	boolean validatePin(String pincode);
}
