package com.team.capestone.test;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TestingFragment extends BaseFragment implements View.OnClickListener {

    private List<QuestionBean> ques;
    private Button btnNext, btnPrevious, btnSubmit;


    private CustomViewpager viewPager;

    private static final String FILENAME = "filename";
    private static final String TITLE = "title";
    private static final String ISEXIST = "isExist";

    private String fileName, title;
    private boolean gotoNext;


    public static TestingFragment newInstance(String fileName, String title, boolean gotoNext) {
        TestingFragment fragment = new TestingFragment();
        Bundle args = new Bundle();
        args.putString(FILENAME, fileName);
        args.putString(TITLE, title);
        args.putBoolean(ISEXIST, gotoNext);
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
        gotoNext = bun.getBoolean(ISEXIST);

        ques = new ArrayList<>();

        ToolbarHelper.set(getActivity(), view.findViewById(R.id.rlToolbar), title);

        viewPager = view.findViewById(R.id.viewPager);
        btnNext = view.findViewById(R.id.btnNext);
        btnPrevious = view.findViewById(R.id.btnPrevious);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnSubmit.setVisibility(View.GONE);

        btnNext.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);
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
                } else {
                    btnSubmit.setVisibility(View.GONE);
                    btnNext.setVisibility(View.VISIBLE);
                }
                viewPos++;

                setQuestions(viewPos);
                break;
            case R.id.btnPrevious:
                if (viewPos == 0) {
                    Toaster.shortToast("This is first question");
                    return;
                }
                viewPos--;
                if (viewPos <= ques.size() - 2) {
                    btnNext.setVisibility(View.VISIBLE);
                    btnSubmit.setVisibility(View.GONE);
                }
                setQuestions(viewPos);
                break;

            case R.id.btnSubmit:
                calculateScore();
                break;

        }


    }

    private void calculateScore() {
        int total = QuestionData.getInstance().getList().size();
        int correctAns = 0;
        for (QuestionBean b : QuestionData.getInstance().getList()) {
            if (TextUtils.equals(b.getSelectedAns(), b.getCorrectAns())) {
                correctAns++;
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Result");
        builder.setCancelable(false);
        if (gotoNext) {
            builder.setMessage(String.format("You've got %s score out of %s in %s. Do you wanna proceed to Test 2 ?", correctAns, total, title));
            builder.setPositiveButton("Yes", (dialog, which) -> {
                dialog.cancel();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flTest, TestingFragment.newInstance("test2.json", "Test 2", false))
                        .commit();
            });
            builder.setNeutralButton("No", (dialog, which) -> {
                getActivity().onBackPressed();
            });
        } else {
            builder.setMessage(String.format("You've got %s score out of %s in %s.", correctAns, total, title));
            builder.setPositiveButton("OK", (dialog, which) -> {
                dialog.cancel();
                getActivity().onBackPressed();
            });
        }

        builder.show();
    }
}
