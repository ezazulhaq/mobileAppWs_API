package com.haa.app.ws.security;

import com.haa.app.ws.SpringApplicationContext;

public class SecurityConstants {
	
	public static final long EXPIRATION_TIME = 864000000; // 10 days
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGNUP_URL = "/users";
	//public static final String TOKEN_SECRET = "asfaskjfgbs76";		// Token secret read from application.properties file
	
	public static String getTokenSecret() {
		
		AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("AppProperties");
		return appProperties.getTokenSceret();
		
	}

}
