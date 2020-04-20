package com.team.capestone.others;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseUser;
import com.team.capestone.base.Toaster;

public class Util {

    public static void saveDetails(FirebaseUser user){
        try {
            String email = user.getEmail();
            SessionManager.getInstance().createLoginSession(email.split("@")[0], email);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showError(@Nullable Exception exception){
        try {
            Toaster.shortToast(((exception == null) || TextUtils.isEmpty(exception.getMessage()))
                    ? " Something went wrong"
                    : exception.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
