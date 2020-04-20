package com.team.capestone.base;

import android.widget.Toast;

public class Toaster {

    public static void shortToast(String msg){
        Toast.makeText(App.get(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void longToast(String msg){
        Toast.makeText(App.get(), msg, Toast.LENGTH_LONG).show();
    }

    public static void somethingWrong() {
        shortToast("Something went wrong.");
    }
}
