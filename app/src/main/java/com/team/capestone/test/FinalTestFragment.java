package com.team.capestone.test;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.team.capestone.R;
import com.team.capestone.base.BaseFragment;
import com.team.capestone.base.Toaster;
import com.team.capestone.base.ToolbarHelper;
import com.team.capestone.others.CustomViewpager;
import com.team.capestone.others.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FinalTestFragment extends BaseFragment implements View.OnClickListener {

    private List<QuestionBean> ques;
    private Button btnNext, btnPrevious, btnSubmit;


    private CustomViewpager viewPager;

    private static final String FILENAME = "filename";
    private static final String TITLE = "title";
    private static final String ISEXIST = "isExist";

    private String fileName, title;
    private boolean gotoNext;


    public static FinalTestFragment newInstance(String fileName, String title) {
        FinalTestFragment fragment = new FinalTestFragment();
        Bundle args = new Bundle();
        args.putString(FILENAME, fileName);
        args.putString(TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_testing, container, false);

        Bundle bun = this.getArguments();
        fileName = bun.getString(FILENAME);
        title = bun.getString(TITLE);

        ques = new ArrayList<>();

        ToolbarHelper.set(getActivity(), view.findViewById(R.id.rlToolbar), title);

        viewPager = view.findViewById(R.id.viewPager);
        btnNext = view.findViewById(R.id.btnNext);
        btnPrevious = view.findViewById(R.id.btnPrevious);
        btnPrevious.setVisibility(View.GONE);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnSubmit.setVisibility(View.GONE);

        btnNext.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        readAllQuestionFromJSON();
        setViewPager();
        return view;

    }

    private void setViewPager() {
        MyPagerAdpater pagerAdpater = new MyPagerAdpater(getChildFragmentManager());
        viewPager.setAdapter(pagerAdpater);
    }

    class MyPagerAdpater extends FragmentStatePagerAdapter {

        public MyPagerAdpater(@NonNull FragmentManager fm) {
            super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return PracticeFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return ques != null ? ques.size() : 0;
        }
    }

    private void setQuestions(int viewPos) {
        viewPager.setCurrentItem(viewPos);
    }

    private void readAllQuestionFromJSON() {
        String jArray = loadJSONFromAsset();
        if (TextUtils.isEmpty(jArray)) {
            Toaster.shortToast("No Question available");
            return;
        }
        try {
            JSONArray arr = new JSONArray(jArray);
            if (arr.length() <= 0) {
                Toaster.shortToast("No Question available");
                return;
            }
            ques.clear();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.optJSONObject(i);
                ques.add(new QuestionBean(o.optInt("Question number"),
                        o.optString("Question"),
                        o.optString("option a"),
                        o.optString("option b"),
                        o.optString("option c"),
                        o.optString("option d"),
                        o.optString("answer")));
            }

            QuestionData.getInstance().addAll(ques);


        } catch (JSONException e) {
            Toaster.somethingWrong();
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public void onClick(View v) {
        int viewPos = viewPager.getCurrentItem();
        switch (v.getId()) {
            case R.id.btnNext:
                if (viewPos >= ques.size() - 2) {
                    btnSubmit.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.GONE);
                }
                viewPos++;
                setQuestions(viewPos);
                break;
            case R.id.btnSubmit:
                calculateScore();
                break;

        }


    }

    private void calculateScore() {
        float total = QuestionData.getInstance().getList().size();
        float correctAns = 0;
        for (QuestionBean b : QuestionData.getInstance().getList()) {
            if (TextUtils.equals(b.getSelectedAns(), b.getCorrectAns())) {
                correctAns++;
            }
        }

        boolean isPassed = false;
        float rem = 0;
        if (correctAns > 0) {
            rem = (correctAns / total);
            isPassed = rem > 0.6;
        }
        showAlertDialogButtonClicked(isPassed, rem);
    }

    public void showAlertDialogButtonClicked(boolean isPassed, float rem) {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // set the custom layout
        builder.setCancelable(false);
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_final_test, null);
        builder.setView(customLayout);
        TextView textView = customLayout.findViewById(R.id.text);
        textView.setText(isPassed ? "Congratulation, You've passed the Test. Your score will be save."
                : "Sorry, You haven't passed TEST. You need to improve.");
        ImageView imageView = customLayout.findViewById(R.id.image);
        imageView.setImageResource(isPassed ? R.drawable.ic_reward : R.drawable.ic_very_sad);

        // add a button
        builder.setPositiveButton("OK", (dialog, which) -> {
            int score = (int) (rem * 100);
            if (isPassed && score >= 60) {
                SessionManager.getInstance().saveLatestScore(score, System.currentTimeMillis());
            }
            dialog.dismiss();
            getActivity().onBackPressed();
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
