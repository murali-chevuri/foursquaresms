package com.apps.foursquare.sms.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import fi.foyt.foursquare.api.FoursquareApi;
import fi.foyt.foursquare.api.FoursquareApiException;
import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.entities.Checkin;
import fi.foyt.foursquare.api.entities.VenuesSearchResult;


public class FourSquareUtils {
	private static final Logger logger =
	      Logger.getLogger(FourSquareUtils.class.getName());
	private static final String CLIENT_ID = "EI5DYEZ2W2WSUMTPCTM0Z3EO4OBDXU2X4I2T5OM51BILKMO0";
	private static final String CLIENT_SECRET = "LG1ECXGQBOQR5TEY2DODOF1BLSUKRZ3G5ZQZP1CWMAYN1VZ2";
	private static final String CALLBACK_URL = "http://foursquaresms.appspot.com/oauth/callback";
	private static final String Y_URL = "http://where.yahooapis.com/geocode?q=$VENUE$";

//	public static Result<VenuesSearchResult> getVenueSearchResults(String venuname){
//		 FoursquareApi foursquareApi = new FoursquareApi(CLIENT_ID, CLIENT_SECRET, CALLBACK_URL);
//		   
//		    // After client has been initialized we can make queries.
//		    Result<VenuesSearchResult> result=null;
//			try {
//				result = foursquareApi.venuesSearch(null, null, null, null, venuname, null, null, null, null, null, null);
//				logger.info("Search Result status code:"+result.getMeta().getCode());
//				if(result.getMeta().getCode()==200) {
//					return result;
//				}
//				logger.log(Level.SEVERE, "Error Type:"+result.getMeta().getErrorType());
//				logger.log(Level.SEVERE, "Error Detail:"+result.getMeta().getErrorDetail());
//			} catch (FoursquareApiException e) {
//				logger.log(Level.SEVERE, e.getMessage(),e);
//				return null;
//			}
//		    return null;
//	}

	public static List<FSVenues> getVenueSearchResults(String venuename){
		String url = Y_URL;
		try {
			url = url.replaceAll("\\$VENUE\\$", URLEncoder.encode(venuename,"UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String xml = null;
		try {
			xml = makerequest(url);
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			return null;
		}
		if(xml.indexOf("Result")<0)
			return null;
			
		List<String> namelist = getTags(xml, "name");
		List<String> latitudelist = getTags(xml, "latitude");
		List<String> longitudelist = getTags(xml, "longitude");
		List<String> citylist = getTags(xml, "city");
		List<FSVenues> venues = new ArrayList<FSVenues>();
		for(int i=0;i<namelist.size();i++) {
			venues.add(new FSVenues(namelist.get(i)+","+citylist.get(i), latitudelist.get(i), longitudelist.get(i)));
		}
		return venues;
		
	}
	
	private static List<String> getTags(String xml, String tag) {
		String starttag = "<"+tag+">";
		String endtag = "</"+tag+">";
		int index  = xml.indexOf(starttag);
		List<String> values = new ArrayList<String>();
		while(index > -1) {
			String value = xml.substring(index+starttag.length(), xml.indexOf(endtag, index));
			values.add(value);
			index = xml.indexOf(starttag, index+starttag.length());
		}
		return values;
	}
	
	private static String makerequest(String urlstr) throws IOException {
	    URL url = new URL(urlstr);
	    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
    	System.out.println("Login http status:"+conn.getResponseCode());
    	
	    // Get the response
	    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	    String line;
	    StringBuffer st = new StringBuffer();
	    while ((line = rd.readLine()) != null) {
	    	st.append(line);
	    }
	    rd.close();
	    return st.toString();
	}

	
	public static Result<Checkin> checkin(String authtoken, String venuId, String venue, String shout, String ll) {
		logger.info("Entered venuID:"+venuId+" venue Name:"+venue+" shout:"+shout);
		 FoursquareApi foursquareApi = new FoursquareApi(CLIENT_ID, CLIENT_SECRET, CALLBACK_URL);
		 foursquareApi.setoAuthToken(authtoken);
		 Result<Checkin> result = null;
		 try {
			result = foursquareApi.checkinsAdd(venuId, venue, shout, null, ll, (double)0, (double)0, (double)0);
			logger.info("Checkin status code:"+result.getMeta().getCode());
			if(result.getMeta().getCode()==200) {
				return result;
			}
			logger.log(Level.SEVERE, "Error Type:"+result.getMeta().getErrorType());
			logger.log(Level.SEVERE, "Error Detail:"+result.getMeta().getErrorDetail());
		} catch (FoursquareApiException e) {
			logger.log(Level.SEVERE,e.getMessage(), e);
		}
		return result;
	}

	public static String getAuthToken(String code) {
		logger.info("Entered code="+code);
		 FoursquareApi foursquareApi = new FoursquareApi(CLIENT_ID, CLIENT_SECRET, CALLBACK_URL);
		 try {
			 foursquareApi.authenticateCode(code);
			 return foursquareApi.getOAuthToken();
		 } catch (FoursquareApiException e) {
			logger.log(Level.SEVERE,e.getMessage(), e);
		}
		return null;
	}
	
	public static String getAuthenticationURL() {
		 FoursquareApi foursquareApi = new FoursquareApi(CLIENT_ID, CLIENT_SECRET, CALLBACK_URL);
		 return foursquareApi.getAuthenticationUrl();
	}
}
