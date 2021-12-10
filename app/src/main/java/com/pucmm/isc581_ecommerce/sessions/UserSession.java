package com.pucmm.isc581_ecommerce.sessions;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import com.pucmm.isc581_ecommerce.Utils.Constants;
import com.pucmm.isc581_ecommerce.activities.LoginActivity;
import com.pucmm.isc581_ecommerce.models.User;

public class UserSession {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context mContext;
    public static final String IS_LOGGED_IN = "isLoggedIn";
    public static final String FIRST_TIME_LOGGED_IN = "firstTimeLoggedIn";

    public static final String KEY_ID = "id";
    public static final String KEY_BIRTHDAY = "birthday";
    public static final String KEY_NAME = "firstName";
    public static final String KEY_LASTNAME = "lastName";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_CONTACT = "contact";
    public static final String KEY_ROL = "rol";
    public static final String KEY_IMAGE = "image";


    public UserSession(Context mContext) {
        this.mContext = mContext;
        sharedPreferences = mContext.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, 0);
        editor = sharedPreferences.edit();
    }

    public void createUserSession (User user) {

        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putLong(KEY_ID, user.getUid());
        editor.putLong(KEY_BIRTHDAY, user.getBirthday());
        editor.putString(KEY_NAME, user.getFirstName());
        editor.putString(KEY_LASTNAME, user.getLastName());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_CONTACT, user.getContact());
        editor.putString(KEY_ROL, user.getRol());
        editor.putString(KEY_IMAGE, user.getImage().toString());


        editor.commit();
    }

    public void updateUserSession (User user) {

        editor.putString(KEY_NAME, user.getFirstName());
        editor.putString(KEY_CONTACT, user.getContact());
        editor.putString(KEY_ROL, user.getRol());
        editor.putString(KEY_IMAGE, user.getImage().toString());


        editor.commit();
    }

    public void checkLogin() {

        if (!this.isLoggedIn()) {
            Intent intent = new Intent(mContext, LoginActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            mContext.startActivity(intent);
        }

    }

    public void logoutUser() {

        editor.putBoolean(IS_LOGGED_IN, false);
        editor.commit();

        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        mContext.startActivity(intent);
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

    public long getUserId() {
        return sharedPreferences.getLong(KEY_ID, -1);
    }

    public String getName() {
        return sharedPreferences.getString(KEY_NAME, null);
    }

    public void setName(String name) {

        editor.putString(KEY_NAME, name);
        editor.commit();
    }

    public String getLastName() {
        return sharedPreferences.getString(KEY_LASTNAME, null);
    }

    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, null);
    }

    public String getContact() {
        return sharedPreferences.getString(KEY_CONTACT, null);
    }

    public String getRol() {
        return sharedPreferences.getString(KEY_ROL, null);
    }

    public void setContact(String phone) {

        editor.putString(KEY_CONTACT, phone);
        editor.commit();
    }

    public String getImage() {
        return sharedPreferences.getString(KEY_IMAGE, null);
    }

    public void setImage(String image) {

        editor.putString(KEY_IMAGE, image);
        editor.commit();
    }

    public boolean isProvider() { return sharedPreferences.getBoolean(KEY_ROL, false);}

    public void setProvider(boolean provider) {

        editor.putBoolean(KEY_ROL, provider);
        editor.commit();
    }

    public Boolean getFirstTime() {
        return sharedPreferences.getBoolean(FIRST_TIME_LOGGED_IN, true);
    }

    public void setFirstTime(Boolean n) {
        editor.putBoolean(FIRST_TIME_LOGGED_IN, n);
        editor.commit();
    }
}