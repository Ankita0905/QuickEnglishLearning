package com.team.capestone.useraction;

import android.util.Patterns;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;
import com.team.capestone.Callback;
import com.team.capestone.base.Toaster;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.team.capestone.others.Util;

public class LoginHelper {

    private FirebaseAuth mAuth;

    public LoginHelper() {
        mAuth = FirebaseAuth.getInstance();
    }

    boolean isValid(EditText etEmail, EditText etPassword, TextInputLayout tilEmail, TextInputLayout tilPswd){
        boolean valid = true;

        if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString().trim()).matches()){
            tilEmail.setErrorEnabled(true);
            tilEmail.setError("Please enter valid email");
            valid = false;
        }else{
            tilEmail.setError(null);
            tilEmail.setErrorEnabled(false);
        }
        if (etPassword.getText().toString().length() < 5){
            tilPswd.setErrorEnabled(true);
            tilPswd.setError("Please enter minimum 6 character.");
            valid = false;
        }else{
            tilPswd.setError(null);
            tilPswd.setErrorEnabled(false);
        }
        return valid;
    }

    void loginWithEmailPassword(EditText etEmail, EditText etPassword, Callback callback) {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    Util.saveDetails(user);
                    Toaster.shortToast("Login Successful");
                    callback.onSuccess(null);
                }else{

                    Util.showError(task.getException());
                }
            }
        });
    }

    void firebaseAuthWithGoogle(@Nullable GoogleSignInAccount acct, Callback callback) {
        if (acct == null){
            Util.showError(null);
            return;
        }
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Sign in success, update UI with the signed-in user's information
                FirebaseUser user = mAuth.getCurrentUser();
                Util.saveDetails(user);
                Toaster.shortToast("Login Successful");
                callback.onSuccess(null);
            } else {
                Util.showError(task.getException());
            }
        });
    }

    void handleFacebookAccessToken(AccessToken accessToken, Callback callback) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                FirebaseUser user = mAuth.getCurrentUser();
                Util.saveDetails(user);
                Toaster.shortToast("Login Successful");
                callback.onSuccess(null);
            }else{
                Util.showError(task.getException());
            }
        });
    }
}
