package com.qbent.enfinsapp.global;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class AuthHelper {
    private static final String JWT_KEY_USERNAME = "username";
    private static final String PREFS = "prefs";
    private static final String PREF_TOKEN = "pref_token";
    private static final String PREF_WORKING_DATE = "pref_working_date";
    private static final String PREF_SELECTION_DATE = "pref_selection_date";
    private static final String PREF_SELECTION_NAME = "pref_selection_name";
    private SharedPreferences mPrefs;

    private static AuthHelper sInstance;

    private AuthHelper(@NonNull Context context) {
        mPrefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        sInstance = this;
    }

    public static AuthHelper getInstance(@NonNull Context context) {
        if (sInstance == null) {
            sInstance = new AuthHelper(context);
        }
        return sInstance;
    }

    public void setIdName(@NonNull String name) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PREF_SELECTION_NAME, name);
        editor.apply();
    }

    public void setIdToken(@NonNull String token) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PREF_TOKEN, token);
        editor.apply();
    }

    public void setIdDate(@NonNull String token) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PREF_WORKING_DATE, token);
        editor.apply();
    }

    public void setIdSelectionDate(@NonNull String token) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PREF_SELECTION_DATE, token);
        editor.apply();
    }

    public void clearIdDate(){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PREF_WORKING_DATE, null);
        editor.commit();
    }

    public void clearIdSelectionDate(){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PREF_SELECTION_DATE, null);
        editor.commit();
    }

    public void clearIdToken(){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PREF_TOKEN, null);
        editor.commit();
    }

    @Nullable
    public String getIdToken(){
        return mPrefs.getString(PREF_TOKEN, null);
    }

    public String getIdName(){
        return mPrefs.getString(PREF_SELECTION_NAME, null);
    }

    public String getIdDate(){
        return mPrefs.getString(PREF_WORKING_DATE, null);
    }

    public String getIdSlectionDate(){
        return mPrefs.getString(PREF_SELECTION_DATE, null);
    }

    public boolean isLoggedIn() {
        String token = getIdToken();
        return token != null;
    }
}
