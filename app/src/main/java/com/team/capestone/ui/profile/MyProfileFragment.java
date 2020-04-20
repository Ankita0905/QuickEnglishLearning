package com.team.capestone.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.se.omapi.Session;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.team.capestone.R;
import com.team.capestone.base.Toaster;
import com.team.capestone.others.CircularTextView;
import com.team.capestone.others.SessionManager;
import com.team.capestone.useraction.LoginActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyProfileFragment extends Fragment {


    private EditText etName, etEmail;
    private TextInputLayout tilName, tilEmail;
    private ProgressBar progressBar;
    private CircularTextView tvName;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            Toaster.shortToast("You're not loggedIn.");
            try {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        tilName = view.findViewById(R.id.tilName);
        etName = view.findViewById(R.id.etName);
        etName.setText(TextUtils.isEmpty(user.getDisplayName()) ? "" : user.getDisplayName());

        tvName = view.findViewById(R.id.tvName);
        setFirstLetter(user);

        tvName.setStrokeWidth(2);
        tvName.setStrokeColor("#388E3C");
        tvName.setSolidColor("#4CAF50");

        tilEmail = view.findViewById(R.id.tilEmail);
        etEmail = view.findViewById(R.id.etEmail);
        etEmail.setText(user.getEmail());
        etEmail.setEnabled(false);

        TextView tvLatestScore = view.findViewById(R.id.tvLatestScore);
        if (SessionManager.getInstance().getScore() >= 60 && SessionManager.getInstance().getTime() > 0 ){
            tvLatestScore.setVisibility(View.VISIBLE);
            SimpleDateFormat sdfDate = new SimpleDateFormat("MMM dd, yy", Locale.getDefault());
            SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            String date= sdfDate.format(new Date(SessionManager.getInstance().getTime()));
            String time= sdfTime.format(new Date(SessionManager.getInstance().getTime()));

            tvLatestScore.setText(String.format("You scored: \t\t%s%%\non %s at %s ", SessionManager.getInstance().getScore(), date, time));
        }else{
            tvLatestScore.setVisibility(View.GONE);
        }

        view.findViewById(R.id.btnUpdate).setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            if (TextUtils.isEmpty(name)){
                tilName.setErrorEnabled(true);
                tilName.setError("Please enter your name");
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            tilName.setError(null);
            tilName.setErrorEnabled(false);

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (task.isSuccessful()) {
                            setFirstLetter(user);
                            Toaster.shortToast("Profile has been updated successfully");
                        }else{
                            Toaster.somethingWrong();
                        }
                    });
        });





        return view;
    }

    private void setFirstLetter(FirebaseUser user) {
        if (TextUtils.isEmpty(user.getDisplayName())){
            try {
                tvName.setVisibility(View.VISIBLE);
                tvName.setText(user.getEmail().substring(0, 1));
            } catch (Exception e) {
                tvName.setVisibility(View.GONE);
            }
        }else{
            tvName.setVisibility(View.VISIBLE);
            tvName.setText(user.getDisplayName().substring(0, 1));
        }
    }
}
