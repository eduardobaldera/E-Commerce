package com.pucmm.isc581_ecommerce.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;

import com.google.android.gms.common.internal.Constants;

public final class Validator {

    private Validator() {

    }

    public static boolean isEmpty (EditText... views) {

        for (EditText view : views) {
            if (TextUtils.isEmpty(view.getText().toString().trim())) {
                view.setError("This field can't be empty");
                view.requestFocus();
                return true;
            }
        }
        return false;
    }

    public static boolean isPasswordConfirmed (EditText pass1, EditText pass2) {
        if(pass1.getText().toString().trim() == pass2.getText().toString().trim()) {
            return true;
        }
        Log.wtf("PASSWORDS", pass1.getText().toString().trim() + "   " + pass2.getText().toString().trim());
        pass2.setError("This field should be identical to Password");
        pass2.requestFocus();
        return false;
    }
    public static boolean isPasswordValid (final EditText passwordView) {

        final String password = passwordView.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordView.setError("You must enter a password");
            passwordView.requestFocus();
            return false;
        } else if (password.length() < 6) {
            passwordView.setError("The password must be at least 6 characters long");
            passwordView.requestFocus();
            return false;
        } else if (!password.matches(".*\\d.*")) {
            passwordView.setError("The password must contain a number");
            passwordView.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean isEmailValid (final EditText emailView) {

        final String email = emailView.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            emailView.setError("You must enter an email address");
            emailView.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailView.setError("You must enter a valid email address");
            emailView.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean isPhoneValid (final EditText phoneView) {

        final String phone = phoneView.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            phoneView.setError("You must enter a phone number");
            phoneView.requestFocus();
            return false;
        } else if (!Patterns.PHONE.matcher(phone).matches()) {
            phoneView.setError("You must enter a valid phone number");
            phoneView.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean isInternetConnected(Context mContext) {
        if (mContext == null) return false;

        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            Log.i("update_status", "Network is available : FALSE");
            return false;
        } else {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true;
                    } else return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
                }
            } else {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                    Log.i("update_status", "Network is available : true");
                    return true;
                }
            }
        }

        return false;
    }
}
