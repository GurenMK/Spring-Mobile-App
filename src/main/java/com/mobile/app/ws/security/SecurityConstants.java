package com.mobile.app.ws.security;

import com.mobile.app.ws.SpringApplicationContext;

public class SecurityConstants {

		public static final long EXPIRATION_TIME = 864000000; //10 days
		public static final String TOKEN_PREFIX = "Bearer ";
		public static final String HEADER_STRING = "Authorization";
		public final static String SIGN_UP_URL = "/users";
		public final static String VERIFICATION_EMAIL_URL = "/users/email-verification";
		
		public static String getTokenSecret() {
			AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("appProperties");
			return appProperties.getTokenSecret();
		}
}
