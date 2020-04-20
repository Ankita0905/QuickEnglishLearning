package com.team.capestone.base;

import android.app.Activity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.team.capestone.R;

public class ToolbarHelper {

    public static void set(Activity activity, RelativeLayout rltoolbar, String title){
        TextView textView = rltoolbar.findViewById(R.id.tvTitle);
        textView.setText(title);

        rltoolbar.findViewById(R.id.ivBack).setOnClickListener(v -> {
            activity.onBackPressed();
        });
    }
}
