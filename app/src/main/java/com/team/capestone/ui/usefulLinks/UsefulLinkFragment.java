package com.team.capestone.ui.usefulLinks;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.team.capestone.Callback;
import com.team.capestone.R;
import com.team.capestone.base.App;
import com.team.capestone.base.BaseFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UsefulLinkFragment extends BaseFragment {
    private RecyclerView rvLinks;
    private JSONArray array = new JSONArray();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_useful_links, container, false);
        rvLinks = view.findViewById(R.id.rvLinks);
        setData();
        return view;
    }

    private void setData() {
        addItem(0,"How to introduce yourself", "https://drive.google.com/file/d/0Bwbky9Sm7K0JRWxpam1XY21lZXc/view?usp=sharing");
        addItem(1,"Sentence Formation", "https://drive.google.com/open?id=0Bwbky9Sm7K0JanRtemVrdUhFWEk");
        addItem(2,"Present Tense", "https://drive.google.com/open?id=0Bwbky9Sm7K0JMGltWEoyaUwxM3M");
        addItem(3,"Past Tense", "https://drive.google.com/open?id=0Bwbky9Sm7K0JYXBFVEtMbElIVWM");
        addItem(4,"Future Tense", "https://drive.google.com/open?id=0Bwbky9Sm7K0JQU5uV2tZODNNNDQ");
        rvLinks.setAdapter(new LinkAdapter(array, new Callback<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                try {
                    gotoLink(array.getJSONObject(integer).optString("link"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }));

    }

    private void gotoLink(String link) {
        Uri uri = Uri.parse(link);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(link)));
        }
    }

    private void addItem(int index, String name, String link){
        try {
            JSONObject object = new JSONObject();
            object.put("name", name);
            object.put("link", link);
            array.put(index, object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
