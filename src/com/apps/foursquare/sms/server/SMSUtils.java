package com.apps.foursquare.sms.server;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Blob;

public class SMSUtils {
	public static enum SMS_STATUS {CREATED, PENDING, SUCCESS};
	private static final Logger logger = Logger.getLogger(SMSUtils.class.getName());
	
	public List<SMSEntity> listSMSEntitys() {
		EntityManager em = EMF.get().createEntityManager();
		// Read the existing entries
		Query q = em.createQuery("select m from SMSEntity m");
		List<SMSEntity> todos = q.getResultList();
		return todos;
	}
	
	public static long insert(String mobilenumber, String message) {
		logger.info("Entere with mobilenumber="+mobilenumber+" message="+message);
		EntityManager em = EMF.get().createEntityManager();
		SMSEntity todo = new SMSEntity(mobilenumber, message, SMS_STATUS.CREATED.toString(), System.currentTimeMillis());
		em.persist(todo);
		em.close();
		return todo.getId();
	}
	
	public static List<SMSEntity> getSMSEntitysByStatus(String status) {
		EntityManager em = EMF.get().createEntityManager();
		Query q = em.createQuery("select t from SMSEntity t where t.status = :status");
		q.setParameter("status", status);
		List<SMSEntity> todos = q.getResultList();
		return todos;
	}

	
	public static List<SMSEntity> getSMSEntitysByMobile(String mobilenumber) {
		EntityManager em = EMF.get().createEntityManager();
		Query q = em.createQuery("select t from SMSEntity t where t.mobilenumber = :mobilenumber");
		q.setParameter("mobilenumber", mobilenumber);
		List<SMSEntity> todos = q.getResultList();
		return todos;
	}
	
	public static int updateSuccess() {
		//TODO update
		return 0;
	}
	
	public void remove(long id) {
		EntityManager em = EMF.get().createEntityManager();
		try {
			SMSEntity todo = em.find(SMSEntity.class, id);
			em.remove(todo);
		} finally {
			em.close();
		}
	}
	
	public static void imageFor(String name, HttpServletResponse res) throws IOException {
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
