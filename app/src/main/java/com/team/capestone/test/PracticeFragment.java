package com.team.capestone.test;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.team.capestone.R;
import com.team.capestone.base.BaseFragment;

public class PracticeFragment extends BaseFragment  {

    private static PracticeFragment instance;
    private static final String POSITION ="pos";

    private TextView tvQues;

    private RadioGroup rgAns;
    private RadioButton rbA, rbB, rbC, rbD;

    private int currentPosition= -1;

    private QuestionBean ques;


    public static PracticeFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        PracticeFragment fragment = new PracticeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_practice, container, false);
        currentPosition = this.getArguments().getInt(POSITION);
        ques = QuestionData.getInstance().get(currentPosition);


        tvQues = view.findViewById(R.id.tvQues);

        rbA = view.findViewById(R.id.rbA);
        rbB = view.findViewById(R.id.rbB);
        rbC = view.findViewById(R.id.rbC);
        rbD = view.findViewById(R.id.rbD);
        rgAns= view.findViewById(R.id.rgAns);
        rgAns.setOnCheckedChangeListener((group, checkedId) -> {
            String selectedAns = "";
            switch (checkedId){
                case R.id.rbA:
                    selectedAns = "a";
                    break;
                case R.id.rbB:
                    selectedAns = "b";
                    break;
                case R.id.rbC:
                    selectedAns = "c";
                    break;
                case R.id.rbD:
                    selectedAns = "d";
                    break;
            }
            ques.setSelectedAns(selectedAns);
            QuestionData.getInstance().set(currentPosition, ques);

        });
        setQuestions();
        return view;
    }

    private void setQuestions() {
        if (ques.getQues().contains(":")){
            String parts[] = ques.getQues().split(":");
            tvQues.setText(String.format("%s. %s:\n%s", ques.getqNo(), parts[0].trim(), parts[1].trim()));
        }else{
            tvQues.setText(String.format("%s. %s", ques.getqNo(), ques.getQues()));
        }
        rbA.setText(ques.getOptionA());
        rbB.setText(ques.getOptionB());
        rbC.setText(ques.getOptionC());
        rbD.setText(ques.getOptionD());
        String selectedAns = ques.getSelectedAns();
        rbA.setChecked(TextUtils.equals("a", selectedAns));
        rbB.setChecked(TextUtils.equals("b", selectedAns));
        rbC.setChecked(TextUtils.equals("c", selectedAns));
        rbD.setChecked(TextUtils.equals("d", selectedAns));
    }
}
