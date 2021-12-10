package com.pucmm.isc581_ecommerce.Utils;

import java.util.Locale;

public final class Constants {

    public static final Locale LOCALE = new Locale("en", "DO");
    public static final int SHARED_PREFERENCES_PRIVATE_MODE = 0;
    public static final String SHARED_PREFERENCES_NAME = "UserSession";
    public static final String API_URL = "http://137.184.110.89:7002";
    public static final String API_LOGIN = "/users/login";
    public static final String API_FORGOT_PASSWORD = "/users/change";
    public static final String API_REGISTER_USER = "/users";
    public static final String API_UPDATE_USER = "/users";
}