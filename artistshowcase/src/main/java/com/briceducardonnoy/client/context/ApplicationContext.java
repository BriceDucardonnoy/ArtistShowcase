package com.briceducardonnoy.client.context;

import java.util.HashMap;

public class ApplicationContext {
	
	private static ApplicationContext instance = null;
	private HashMap<String, Object> userObjects;
	
	public static final String DETAIL_KEYWORD	= "picture";
	public static final String PHOTOSFOLDER 	= "photos";
	public static final String FILEINFO		= "fileInfo";
	
	private ApplicationContext() {
		userObjects = new HashMap<String, Object>();
	}
	
	public static ApplicationContext getInstance() {
		if(instance == null) {
			instance = new ApplicationContext();
		}
		return instance;
	}
	
	public void addProperty(String key, Object value) {
		userObjects.put(key, value);
	}
	
	public Object getProperty(String key) {
		return userObjects.get(key);
	}
	
}
