package com.apps.foursquare.sms.server;

import java.util.HashMap;
import java.util.Map;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;

import com.google.appengine.api.memcache.jsr107cache.GCacheFactory;

public class CacheUtils {
	private static final int EXPIRATION_DELTA =3600; 
	
	private static Cache getCache() throws CacheException {
		Map props = new HashMap();
        props.put(GCacheFactory.EXPIRATION_DELTA, EXPIRATION_DELTA);
        Cache cache=null;
    	CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
        cache = cacheFactory.createCache(props);
        return cache;
	}
	
	public static void put(String key, Object object) throws CacheException {
		getCache().put(key, object);
	}

	
	public static Object get(String key) throws CacheException {
		return getCache().get(key);
	}
}
