package com.team.capestone.ui.usefulLinks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.team.capestone.Callback;
import com.team.capestone.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LinkAdapter extends RecyclerView.Adapter<LinkAdapter.Holder> {
    private JSONArray array;
    private Callback<Integer> callback;

    public LinkAdapter(JSONArray array, Callback<Integer> callback) {
        this.array = array;
        this.callback = callback;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_link, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        try {
            JSONObject object = array.getJSONObject(position);
            holder.text.setText(object.optString("name", ""));
            holder.itemView.setOnClickListener(v -> {callback.onSuccess(holder.getAdapterPosition());});
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return array != null ?array.length() : 0;
    }

    public class Holder extends RecyclerView.ViewHolder {

        private final TextView text;
        public Holder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
        }
    }
}
