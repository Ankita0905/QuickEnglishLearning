package com.team.capestone.useraction;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.team.capestone.R;
import com.team.capestone.base.BaseActivity;
import com.team.capestone.base.Toaster;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        EditText etEmail = findViewById(R.id.etEmail);
        findViewById(R.id.btnSendLink).setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                etEmail.setError("Please enter valid email");
                return;
            }
            etEmail.setError(null);
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toaster.shortToast("Please check your email to reset your password");
                        }else{
                            Exception exception = task.getException();
                            Toaster.shortToast(((exception == null) || TextUtils.isEmpty(exception.getMessage()))
                                    ? " Something went wrong"
                                    : exception.getMessage());
                        }
                    });
        });
    }
}
