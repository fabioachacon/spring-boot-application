package com.parent.ws.app.security;

public class SecurityConstants {
    public static final long EXPIRATION_DATE = 864000000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/user";
    public static final String LOGIN_URL = SIGN_UP_URL + "/login";
}
