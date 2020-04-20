package com.team.capestone.ui.home;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.team.capestone.R;
import com.team.capestone.test.TestActivity;
import com.team.capestone.base.BaseFragment;

public class ChooseTestFragment extends BaseFragment {

    public static ChooseTestFragment newInstance() {

        Bundle args = new Bundle();

        ChooseTestFragment fragment = new ChooseTestFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_test, container, false);
        view.findViewById(R.id.btnFinal).setOnClickListener(v -> {
            TestActivity.startActivity(getContext(), 2);
        });
        view.findViewById(R.id.btnPractice).setOnClickListener(v -> {


            new AlertDialog.Builder(getActivity())
                    .setTitle("Choose Test")
                    .setItems(new String[]{"Test 1", "Test 2"}, (dialog, which) -> {
                        dialog.dismiss();
                        if (which==0){
                            TestActivity.startActivity(getContext(), 0);
                        }else if (which == 1){
                            TestActivity.startActivity(getContext(), 1);
                        }
                    })
                    .show();
        });
        return view;
    }
}
