package com.apps.foursquare.sms.server;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class SubscriberUtils {
	
	private static final Logger logger = Logger.getLogger(SubscriberUtils.class.getName());
	public static enum SUB_STATUS {REG_PENDING, SMS_REG_PENDING, SUCCESS};
	
	
	public List<SubscriberEntity> listSubscriberEntitys() {
		EntityManager em = EMF.get().createEntityManager();
		// Read the existing entries
		Query q = em.createQuery("select m from SubscriberEntity m");
		List<SubscriberEntity> subscribers = q.getResultList();
		return subscribers;
	}
	
	public static long insert(String mobilenumber, String status, String authToken, String code) {
		EntityManager em = EMF.get().createEntityManager();
		SubscriberEntity subscriber = new SubscriberEntity(mobilenumber, status, authToken, code);
		em.persist(subscriber);
		em.close();
		return subscriber.getId();
	}
	
	public static SubscriberEntity getSubscriber(String mobilenumber) {
		EntityManager em = EMF.get().createEntityManager();
		Query q = em.createQuery("select t from SubscriberEntity t where t.mobilenumber = :mobilenumber");
		q.setParameter("mobilenumber", mobilenumber);
		List<SubscriberEntity> subscribers = q.getResultList();
		if(subscribers.size()==0)
			return null;
		return subscribers.get(0);
	}
	
	public static List<SubscriberEntity> getSubscriberEntitysByStatus(String status) {
		EntityManager em = EMF.get().createEntityManager();
		Query q = em.createQuery("select t from SubscriberEntity t where t.status = :status");
		q.setParameter("status", status);
		List<SubscriberEntity> subscribers = q.getResultList();
		return subscribers;
	}
	
	public static void remove(String mobilenumber) {
		EntityManager em = EMF.get().createEntityManager();
		try {
			SubscriberEntity subscriber = em.find(SubscriberEntity.class, mobilenumber);
			em.remove(subscriber);
		} finally {
			em.close();
		}
	}
	
	public static void updateStatus(String mobilenumber, String status){
		logger.info("Entered with mobilenumber="+mobilenumber+" status="+status);
		EntityManager em = EMF.get().createEntityManager();
		try {
			SubscriberEntity sub = em.find(SubscriberEntity.class, mobilenumber);
			sub.setStatus(status);
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
	}

}
