package com.team.capestone.others;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.team.capestone.base.App;
import com.team.capestone.useraction.LoginActivity;

import java.util.HashMap;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;
     
    // Editor for Shared preferences
    private SharedPreferences.Editor editor;
     
    // Shared pref mode
    int PRIVATE_MODE = 0;
     
    // Sharedpref file name
    private static final String PREF_NAME = "com.team.capestone";
     
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
     
    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";
     
    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    private static final String KEY_SCORE = "key_score";
    private static final String KEY_TIME = "key_time";

    private static SessionManager instance;
    // Constructor
    private SessionManager(){

        pref =App.get().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public synchronized static SessionManager getInstance() {
        if (instance== null){
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String name, String email){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
         
        // Storing name in pref
        editor.putString(KEY_NAME, name);
         
        // Storing email in pref
        editor.putString(KEY_EMAIL, email);
         
        // commit changes
        editor.commit();
    }   
     

     
     
     
    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
         
        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
         
        // return user
        return user;
    }
     

     
    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void logout() {
        editor.clear();
        editor.commit();
    }

    public void saveLatestScore(int score, long currentTimeMillis) {
        editor.putInt(KEY_SCORE, score);
        editor.putLong(KEY_TIME, currentTimeMillis);
        editor.commit();
    }

    public int getScore() {
        return pref.getInt(KEY_SCORE, 0);
    }

    public long getTime() {
        return pref.getLong(KEY_TIME, 0);
    }
}