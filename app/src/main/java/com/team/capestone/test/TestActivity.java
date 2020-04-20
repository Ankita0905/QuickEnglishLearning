package com.team.capestone.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.team.capestone.R;
import com.team.capestone.base.BaseActivity;
import com.team.capestone.base.BaseFragment;

public class TestActivity extends BaseActivity {

    public static final String TEST_TYPE = "testType";
    public static void startActivity(Context ctx, int testType){
        ctx.startActivity(new Intent(ctx, TestActivity.class ).putExtra(TEST_TYPE, testType));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        int testType = getIntent().getIntExtra(TEST_TYPE, -1);
        BaseFragment fragment= null;
        if (testType== 0){
            fragment = TestingFragment.newInstance("test1.json", "Test 1", true);
        }else if (testType == 1){
            fragment = TestingFragment.newInstance("test2.json", "Test 2", false);
        }else{
            fragment = FinalTestFragment.newInstance("final_test.json", "Final Test");
        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.flTest, fragment)
                .commit();

    }
}
