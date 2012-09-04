package com.apps.foursquare.sms.server.servlet;

import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.apps.foursquare.sms.server.FourSquareUtils;
import com.apps.foursquare.sms.server.SMSUtils;
import com.apps.foursquare.sms.server.SubscriberUtils;

public class CallBack extends HttpServlet {
	
	private static final Logger logger =
	      Logger.getLogger(CallBack.class.getName());
	//http://foursquaresms.appspot.com/oauth/callback?code=<code>
	
	private static final String HOME_URL = "http://foursquaresms.appspot.com";
	private static final String TECHDIFF_MSG = "Sorry, We are facing technical difficulty. Please register at http://foursquaresms.appspot.com";
	private static final String REGISTRATION_MSG = "Please verify your mobilenumber at http://foursquaresms.appspot.com. You verification code ";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException{
		doAction(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws java.io.IOException, ServletException{
		doAction(request,response);
	}
	
	private void doAction(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
		logger.log(Level.INFO, "Entered");
		Enumeration reqenu = request.getParameterNames();
		while(reqenu.hasMoreElements()) {
			String para = reqenu.nextElement().toString();
			logger.info("Request Parameter name="+para+" Value="+request.getParameter(para));
		}
		
		String mobilenumber = null;
		HttpSession session = request.getSession();
		if(session.getAttribute("mobilenumber")!=null)
			mobilenumber = session.getAttribute("mobilenumber").toString();
		else {
			logger.log(Level.SEVERE, "Mobilenumber is null");
			return;
		}
		String code = request.getParameter("code");
		logger.log(Level.INFO, "Mobile="+mobilenumber+" Code="+code);
		if(mobilenumber==null || code==null) {
			logger.log(Level.SEVERE, "Code is null.");
			SMSUtils.insert(mobilenumber, TECHDIFF_MSG);
			return;
		}
		String authtoken = FourSquareUtils.getAuthToken(code);
		logger.info("AuthToken: "+authtoken);
		if(authtoken == null) {
			logger.log(Level.INFO, "Not registered.");
			SMSUtils.insert(mobilenumber, TECHDIFF_MSG);
			return;
		}
		long verificationcode = SubscriberUtils.insert(mobilenumber, 
				SubscriberUtils.SUB_STATUS.SMS_REG_PENDING.toString(), authtoken, code);
		logger.info("Verification code for "+mobilenumber+" is "+verificationcode);
		SMSUtils.insert(mobilenumber, REGISTRATION_MSG+verificationcode);
		response.sendRedirect(HOME_URL);
	}

}
