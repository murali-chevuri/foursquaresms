package com.apps.foursquare.sms.server.servlet;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.apps.foursquare.sms.server.EMF;
import com.apps.foursquare.sms.server.FourSquareUtils;
import com.apps.foursquare.sms.server.MyImage;
import com.apps.foursquare.sms.server.SMSEntity;
import com.apps.foursquare.sms.server.SMSUtils;
import com.apps.foursquare.sms.server.SubscriberUtils;
import com.google.appengine.api.datastore.Blob;

public class DisplayImage extends HttpServlet {
	
	private static final Logger logger =
	      Logger.getLogger(DisplayImage.class.getName());
	//http://foursquaresms.appspot.com/uploadimage

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
	
	private void doAction(HttpServletRequest req, HttpServletResponse res) throws java.io.IOException, ServletException, FileUploadException {
		logger.log(Level.INFO, "Entered");
		String name = req.getParameter("name");
		logger.log(Level.INFO, "Name: "+name);
	    // find desired image
	    EntityManager em = EMF.get().createEntityManager();
	    Query query = em.createQuery("select t from MyImage t where t.name = :nameParam");
	    query.setParameter("nameParam", name);
	    List<MyImage> results = (List<MyImage>)query.getResultList();
	    Blob image = results.iterator().next().getImage();

	    // serve the first image
	    res.setContentType("image/jpeg");
	    res.getOutputStream().write(image.getBytes());
	
	}

}
