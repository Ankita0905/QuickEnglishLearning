package com.team.capestone.useraction;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;
import com.team.capestone.Callback;
import com.team.capestone.base.Toaster;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupHelper {
    private static final String TAG = "SignupHelper";
    private FirebaseAuth mAuth;
    public SignupHelper() {
        mAuth = FirebaseAuth.getInstance();
    }
    boolean isValid(EditText etEmail, EditText etPassword, EditText etCPassword, TextInputLayout tilEmail, TextInputLayout tilPswd, TextInputLayout tilRePswd){
        boolean valid = true;

        if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString().trim()).matches()){
            tilEmail.setErrorEnabled(true);
            tilEmail.setError("Please enter valid email");
            valid = false;
        }else{
            tilEmail.setError(null);
            tilEmail.setErrorEnabled(false);
        }
        String pswd = etPassword.getText().toString();
        if (pswd.length() < 6){
            tilPswd.setErrorEnabled(true);
            tilPswd.setError("Please enter minimum 6 character.");
            valid = false;
        }else{
            tilPswd.setError(null);
            tilPswd.setErrorEnabled(false);
        }
        String rePswd = etCPassword.getText().toString();
        if (rePswd.length() < 6 || !TextUtils.equals(rePswd, pswd)){
            tilRePswd.setErrorEnabled(true);
            tilRePswd.setError("RePassword must be same as Password.");
            valid = false;
        }else{
            tilRePswd.setError(null);
            tilRePswd.setErrorEnabled(false);
        }
        return valid;
    }

    void createUserWithEmailPassword(EditText etEmail, EditText etPassword, Callback callback) {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toaster.shortToast("Signup Successful");
                    callback.onSuccess(null);
                }else{
                    Log.e(TAG, "onComplete: "+task.getException().getMessage());
                    Toaster.shortToast("Authentication failed.");
                }
            }
        });
    }

}
