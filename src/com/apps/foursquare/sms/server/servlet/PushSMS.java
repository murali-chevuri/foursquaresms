package com.apps.foursquare.sms.server.servlet;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jsr107cache.CacheException;

import com.apps.foursquare.sms.server.CacheUtils;
import com.apps.foursquare.sms.server.FSVenues;
import com.apps.foursquare.sms.server.FourSquareUtils;
import com.apps.foursquare.sms.server.SMSUtils;
import com.apps.foursquare.sms.server.SubscriberEntity;
import com.apps.foursquare.sms.server.SubscriberUtils;

import fi.foyt.foursquare.api.FoursquareApi;
import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.entities.Checkin;
import fi.foyt.foursquare.api.entities.CompactVenue;
import fi.foyt.foursquare.api.entities.VenuesSearchResult;

public class PushSMS extends HttpServlet {
	private static final Logger logger =
	      Logger.getLogger(PushSMS.class.getName());
	//http://foursquaresms.appspot.com/pushsms?mobilenumber=<mobilenumber>&msg=<msg>
	private static final String NOT_REG_SMS = "Please register at http://foursquaresms.appspot.com";
	private static final String INVALID_SMS = "Invalid message.";
	private static final String INVALID_PIN = "Invalid PIN.";
	private static final String REG_SUCCESS = "Registration Successful.";
	private static final String TECHDIFF_MSG = "Sorry, We are facing technical difficulty.";
	private static final String CHECKIN_SUCCESS_MSG = "Check in succesfull.";
	private static final int SMS_SIZE_LIMIT = 160;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException{
		doAction(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws java.io.IOException, ServletException{
		doAction(request,response);
	}
	
	private void doAction(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
		logger.log(Level.INFO, "Entered");
		String mobilenumber = request.getParameter("mobilenumber");
		String msg = request.getParameter("msg");
		logger.log(Level.INFO, "Mobile:"+mobilenumber+" Msg:"+msg);
		SubscriberEntity subscriber = SubscriberUtils.getSubscriber(mobilenumber);
		if(subscriber==null||subscriber.getAuthToken()==null) {
			logger.log(Level.INFO, "Not registered.");
			SMSUtils.insert(mobilenumber, NOT_REG_SMS);
			return;
		} else if(msg==null||msg.trim().equalsIgnoreCase("")) {
			logger.log(Level.INFO, "Invalid SMS.");
			SMSUtils.insert(mobilenumber, INVALID_SMS);
			return;
		}
		msg = msg.trim();
		if(msg.toUpperCase().startsWith("PIN")) {
			String pin = msg.substring(3, msg.length());
			if(pin==null||!pin.trim().equalsIgnoreCase(subscriber.getCode())) {
				logger.log(Level.INFO, "Invalid PIN.");
				SMSUtils.insert(mobilenumber, INVALID_PIN);
			} else {
				logger.log(Level.INFO, "Successfully registerd.");
				SubscriberUtils.updateStatus(mobilenumber, SubscriberUtils.SUB_STATUS.SUCCESS.toString());
				SMSUtils.insert(mobilenumber, REG_SUCCESS);
			}
			return;
		} else if(msg.startsWith("@")) {
			String venue = msg.substring(1);
			List<FSVenues> searchresult = FourSquareUtils.getVenueSearchResults(venue);
			if(searchresult==null) {
				logger.log(Level.SEVERE, "Technical Difficulty. Search Result is null.");
				SMSUtils.insert(mobilenumber, TECHDIFF_MSG);
				return;
			}
			logger.info("Venue Result length:"+searchresult.size());
			if(searchresult.size()>1) {
				try {
					CacheUtils.put(mobilenumber, searchresult);
				} catch (CacheException e) {
					logger.log(Level.SEVERE, e.getMessage(), e);
					SMSUtils.insert(mobilenumber, TECHDIFF_MSG);
					return;
				}
				String sms = buildSMSForVenues(searchresult);
				SMSUtils.insert(mobilenumber, sms);
				return;
			}

			Result<Checkin> resultcheckin=null;
			if(searchresult.size()==0) {
				logger.info("No Venue Result found");
				resultcheckin = FourSquareUtils.checkin(subscriber.getAuthToken(), null, venue, null, null);
			} if(searchresult.size()==1) {
				resultcheckin = FourSquareUtils.checkin(subscriber.getAuthToken(), null, searchresult.get(0).getName(), null,
						searchresult.get(0).getLatitude()+","+searchresult.get(0).getLongitude());
			}
			if(resultcheckin ==null) {
				SMSUtils.insert(mobilenumber, TECHDIFF_MSG);
			} else {
				SMSUtils.insert(mobilenumber, buildSMSForCheckin(resultcheckin));
			}
			return;
		}

		try {
			int index = Integer.parseInt(msg);
			List<FSVenues> searchresult = (List<FSVenues>)CacheUtils.get(mobilenumber);
			if(index>0 && index <= searchresult.size()) {
				FSVenues venue = searchresult.get(index-1);
				Result<Checkin> resultcheckin = FourSquareUtils.checkin(subscriber.getAuthToken(), null, venue.getName(), null
						, venue.getLatitude()+","+venue.getLongitude());
				if(resultcheckin ==null) {
					SMSUtils.insert(mobilenumber, TECHDIFF_MSG);
				} else {
					SMSUtils.insert(mobilenumber, buildSMSForCheckin(resultcheckin));
				}
			}
		}catch(Exception e) {	}
		SMSUtils.insert(mobilenumber, INVALID_SMS);
	}
	
	private String buildSMSForVenues(List<FSVenues> searchresult) {
		String sms = "";
		FSVenues []venues = searchresult.toArray(new FSVenues[0]);
		for (int i=0;i<venues.length && sms.length()<=SMS_SIZE_LIMIT;i++) {
			sms = sms + (i+1) +" " + venues[i].getName() +"\n";
	    }
		return sms;
	}
	
	private String buildSMSForCheckin(Result<Checkin> result) {
		String sms = CHECKIN_SUCCESS_MSG;
		Checkin checkin = result.getResult();
		if(checkin.getIsMayor()) {
			sms = sms + "Congrats you are Mayor for "+checkin.getVenue().getName();
		}
		return sms;
	}
	

}
