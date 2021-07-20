package com.henribambangs.kapronpetshop.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.henribambangs.kapronpetshop.LoginActivity;
import com.henribambangs.kapronpetshop.Model.ModelUser;

import static android.accounts.AccountManager.KEY_PASSWORD;

public class AdapterUser {

    //the constants
    private static final String SHARED_PREF_NAME = "customerinfo";
    private static final String KEY_ID = "KEY_ID";
    private static final String KEY_USERNAME = "KEY_USERNAME";
    private static final String KEY_NAME = "KEY_NAME";
    private static final String KEY_EMAIL = "KEY_EMAIL";
    private static final String KEY_GENDER = "KEY_GENDER";
    private static final String KEY_ADDRESS = "KEY_ADDRESS";
    private static final String KEY_PHONE = "KEY_PHONE";
    private static final String KEY_IMAGE = "KEY_IMAGE";
    private static final String KEY_PROV = "KEY_PROV";
    private static final String KEY_CITY = "KEY_CITY";
    private static final String KEY_SUBDIST = "KEY_SUBDIST";
    private static final String KEY_PROVNAME = "KEY_PROVNAME";
    private static final String KEY_CITYNAME = "KEY_CITYNAME";
    private static final String KEY_SUBDISTNAME = "KEY_SUBDISTNAME";

    @SuppressLint("StaticFieldLeak")
    private static AdapterUser mInstance;
    @SuppressLint("StaticFieldLeak")
    private static Context mCtx;

    private AdapterUser(Context context) {
        mCtx = context;
    }

    public static synchronized AdapterUser getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new AdapterUser(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(ModelUser user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getCustomer_id());
        editor.putString(KEY_USERNAME, user.getLogin_id());
        editor.putString(KEY_NAME, user.getCustomer_name());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_GENDER, user.getGender());
        editor.putString(KEY_ADDRESS, user.getAddress());
        editor.putString(KEY_PHONE, user.getPhone_number());
        editor.putString(KEY_IMAGE, user.getCustomer_image());
        editor.putString(KEY_PROV, user.getProvince_id());
        editor.putString(KEY_CITY, user.getCity_id());
        editor.putString(KEY_SUBDIST, user.getSubdistrict_id());
        editor.putString(KEY_PROVNAME, user.getProvince_name());
        editor.putString(KEY_CITYNAME, user.getCity_name());
        editor.putString(KEY_SUBDISTNAME, user.getSubdistrict_name());
        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    //this method will give the logged in user
    public ModelUser getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new ModelUser(
                sharedPreferences.getInt(KEY_ID, 0),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_NAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_GENDER, null),
                sharedPreferences.getString(KEY_ADDRESS, null),
                sharedPreferences.getString(KEY_PHONE, null),
                sharedPreferences.getString(KEY_IMAGE, null),
                sharedPreferences.getString(KEY_PROV, null),
                sharedPreferences.getString(KEY_CITY, null),
                sharedPreferences.getString(KEY_SUBDIST, null),
                sharedPreferences.getString(KEY_PROVNAME, null),
                sharedPreferences.getString(KEY_CITYNAME, null),
                sharedPreferences.getString(KEY_SUBDISTNAME, null)
        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LoginActivity.class));
    }
}
