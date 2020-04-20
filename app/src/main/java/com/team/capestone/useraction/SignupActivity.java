package com.team.capestone.useraction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;
import com.team.capestone.Callback;
import com.team.capestone.MainActivity;
import com.team.capestone.R;
import com.team.capestone.base.BaseActivity;

public class SignupActivity  extends BaseActivity {

    private SignupHelper helper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        helper = new SignupHelper();
        EditText email = findViewById(R.id.etEmail);
        EditText pswd = findViewById(R.id.etPassword);
        EditText rePswd = findViewById(R.id.etCPassword);
        TextInputLayout tilEmail = findViewById(R.id.tilEmail);
        TextInputLayout tilPswd = findViewById(R.id.tilPswd);
        TextInputLayout tilRePswd = findViewById(R.id.tilRePswd);
        findViewById(R.id.btnSignUp).setOnClickListener(v -> {
             if (helper.isValid(email, pswd, rePswd, tilEmail, tilPswd , tilRePswd)){
                 helper.createUserWithEmailPassword(email, pswd, new Callback() {
                     @Override
                     public void onSuccess(Object o) {
                         startActivity(new Intent(SignupActivity.this, MainActivity.class));
                         SignupActivity.this.finish();
                     }
                 });
             }

        });

        findViewById(R.id.tvSignIn).setOnClickListener(v -> {
            onBackPressed();
        });
    }
}
