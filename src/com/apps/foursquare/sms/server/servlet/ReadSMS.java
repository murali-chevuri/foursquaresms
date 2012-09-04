package com.apps.foursquare.sms.server.servlet;

import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.apps.foursquare.sms.server.FourSquareUtils;
import com.apps.foursquare.sms.server.SMSEntity;
import com.apps.foursquare.sms.server.SMSUtils;
import com.apps.foursquare.sms.server.SubscriberUtils;

public class ReadSMS extends HttpServlet {
	
	private static final Logger logger =
	      Logger.getLogger(ReadSMS.class.getName());
	//http://foursquaresms.appspot.com/readsms

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException{
		try {
			doAction(request,response);
		}catch(Throwable t) {
			t.printStackTrace();
			logger.log(Level.SEVERE, t.getMessage(), t);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws java.io.IOException, ServletException{
		try {
			doAction(request,response);
		}catch(Throwable t) {
			t.printStackTrace();
			logger.log(Level.SEVERE, t.getMessage(), t);
		}
	}
	
	private void doAction(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
		logger.log(Level.INFO, "Entered");
		Enumeration reqenu = request.getParameterNames();
		while(reqenu.hasMoreElements()) {
			String para = reqenu.nextElement().toString();
			logger.info("Request Parameter name="+para+" Value="+request.getParameter(para));
		}
		
		String status = SMSUtils.SMS_STATUS.CREATED.toString();
		if(request.getParameter(status)!=null)
			status = request.getParameter("status");
		logger.log(Level.INFO, "status="+status);
		StringBuffer st = new StringBuffer();
		List<SMSEntity> list = SMSUtils.getSMSEntitysByStatus(status);
		for(SMSEntity entity:list) {
			st.append(entity.getMobilenumber()+","+entity.getMessage()+"\n");
		}
		SMSUtils.updateSuccess();
		response.getOutputStream().write(st.toString().getBytes());
	}

}
