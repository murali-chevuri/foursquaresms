package com.apps.foursquare.sms.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.apps.foursquare.sms.client.GreetingService;
import com.apps.foursquare.sms.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	public String getSubscriberStatus(String mobilenumber) throws IllegalArgumentException {
		// Verify that the input is valid. 
		if (!FieldVerifier.isValidName(mobilenumber)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException(
					"Mobilenumber must be at least 9 characters long");
		}


		// Escape data from the client to avoid cross-site script vulnerabilities.
		mobilenumber = escapeHtml(mobilenumber);
		
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		session.setAttribute("mobilenumber", mobilenumber);
		
		SubscriberEntity entity = SubscriberUtils.getSubscriber(mobilenumber);
		if(entity==null)
			return FourSquareUtils.getAuthenticationURL();
		else 
			return entity.getStatus();

	}
	
	public boolean validatePin(String pincode) {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		String mobilenumber = session.getAttribute("mobilenumber").toString();
		SubscriberEntity entity = SubscriberUtils.getSubscriber(mobilenumber);
		if(!pincode.equalsIgnoreCase(entity.getId()+"")) {
			return false;
		}
		SubscriberUtils.updateStatus(entity.mobilenumber, SubscriberUtils.SUB_STATUS.SUCCESS.toString());
		return true;
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}
}
