package com.team.capestone.useraction;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.team.capestone.Callback;

import com.team.capestone.MainActivity;
import com.team.capestone.R;
import com.team.capestone.base.BaseActivity;
import com.team.capestone.base.Toaster;


import com.team.capestone.others.RememberMe;
import com.team.capestone.others.SessionManager;
import com.team.capestone.others.Util;

import java.util.Arrays;

public class LoginActivity extends BaseActivity implements FacebookCallback<LoginResult> {

    private EditText etEmail, etPassword;
    private TextInputLayout tilEmail, tilPswd;

    private LoginHelper helper;
    private GoogleSignInClient mGoogleSignInClient;

    private final  int RC_SIGN_IN = 1001;
    private CallbackManager callbackManager;
    private CheckBox cbRememberMe;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SessionManager.getInstance().isLoggedIn()){
            startActivity(new Intent(LoginActivity.this, MainActivity.class ));
            finish();
        }
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();

        helper = new com.team.capestone.useraction.LoginHelper();
        Button btnLogin = findViewById(R.id.btnLogin);

        etEmail = findViewById(R.id.etEmail);
        tilEmail = findViewById(R.id.tilEmail);

        etPassword = findViewById(R.id.etPassword);
        tilPswd = findViewById(R.id.tilPswd);

        etEmail.setText(RememberMe.getInstance().getEmail());
        etPassword.setText(RememberMe.getInstance().getPswd());

        cbRememberMe = findViewById(R.id.cbRememberMe);

        btnLogin.setOnClickListener(v -> {

            if (helper.isValid(etEmail, etPassword, tilEmail, tilPswd)){
                isRememberMe(etEmail, etPassword);
                helper.loginWithEmailPassword(etEmail, etPassword,callback);
            }

        });

        findViewById(R.id.tvForgetPassword).setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, com.team.capestone.useraction.ForgetPassword.class));
        });


        findViewById(R.id.tvSignup).setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, com.team.capestone.useraction.SignupActivity.class));
        });

        SignInButton signInButton = findViewById(R.id.sign_in_button);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1016242051916-s31vap11pvrgn6vk0mr9ftuq372n3r2o.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signInButton.setOnClickListener(v -> {
            signIn();
        });

        findViewById(R.id.facebookLogin).setOnClickListener(v -> {
            LoginManager.getInstance().logOut();
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
            LoginManager.getInstance().registerCallback(callbackManager, this);
        });



    }

    private void isRememberMe(EditText etEmail, EditText etPassword) {
        if (cbRememberMe.isChecked()){
            RememberMe.getInstance().saveDetails(etEmail.getText().toString().trim(), etPassword.getText().toString());
        }else{
            RememberMe.getInstance().clearRememberMe();
        }
    }

    private Callback callback = new Callback() {
        @Override
        public void onSuccess(Object o) {
            goToMainUI();
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        try {
            if (mGoogleSignInClient != null) {
                mGoogleSignInClient.signOut();
            }
            LoginManager.getInstance().logOut();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goToMainUI(){
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        LoginActivity.this.finish();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                helper.firebaseAuthWithGoogle(account, callback);
            } catch (ApiException e) {
                Util.showError(e);
            }
        }
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        helper.handleFacebookAccessToken(loginResult.getAccessToken(), callback);
    }

    @Override
    public void onCancel() {
        Toaster.shortToast("Login with Facebook canceled");
    }

    @Override
    public void onError(FacebookException error) {
        Toaster.shortToast(error == null || TextUtils.isEmpty(error.getMessage())
                ? "Something went wrong"
                : error.getMessage());
    }
}
