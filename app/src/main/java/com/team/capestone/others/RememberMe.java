package com.team.capestone.others;

import android.content.SharedPreferences;

import com.team.capestone.base.App;

public class RememberMe {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    private SharedPreferences.Editor editor;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "com.team.capestone.rememberme";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PSWD = "pswd";

    // Email address (make variable public to access from outside)

    private static RememberMe instance;
    // Constructor
    private RememberMe(){

        pref = App.get().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public synchronized static RememberMe getInstance() {
        if (instance== null){
            instance = new RememberMe();
        }
        return instance;
    }

    public void clearRememberMe() {
        editor.clear();
        editor.commit();
    }

    public void saveDetails(String email, String password) {
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PSWD, password);
        editor.commit();
    }

    public String getEmail() {
        return pref.getString(KEY_EMAIL, "");
    }

    public String getPswd(){
        return pref.getString(KEY_PSWD, "");
    }
}