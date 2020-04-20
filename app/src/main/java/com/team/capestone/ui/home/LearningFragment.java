package com.team.capestone.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.team.capestone.R;
import com.team.capestone.base.BaseFragment;
import com.team.capestone.base.Toaster;
import com.team.capestone.models.AddWords;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class LearningFragment extends BaseFragment  implements TextToSpeech.OnInitListener {

    private static final String TAG = "LearningFragment";
    private static LearningFragment instance;
    private TextToSpeech tts;
    private TextView tvWord, tvMeaning, tvUse;
    private ProgressBar progressBar;
    private RelativeLayout rlMain;

//    public static LearningFragment newInstance() {
//
//        if (instance == null) {
//            Bundle args = new Bundle();
//
//            instance = new LearningFragment();
//            instance.setArguments(args);
//        }
//        return instance;
//    }


    public static LearningFragment newInstance() {
        return new LearningFragment();
    }

    int pos=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learning, container, false);
        tts = new TextToSpeech(getContext(), this);
        tvWord = view.findViewById(R.id.tvWord);
        tvUse = view.findViewById(R.id.tvUse);
        tvMeaning = view.findViewById(R.id.tvMeaning);
        progressBar = view.findViewById(R.id.progressBar);
        rlMain = view.findViewById(R.id.rlMain);
        rlMain.setVisibility(View.GONE);
        view.findViewById(R.id.btnPlay).setOnClickListener(v -> {
            playWord(tvWord.getText());
        });
        view.findViewById(R.id.btnNext).setOnClickListener(v -> setValues());
        getAllWordsFromFirebase();

        return view;

    }

    private void setValues() {
        try {
            Log.e(TAG, "setValues: "+pos);
            if (pos >= (words.size()-1)) {
                pos=0;
            }
            AddWords w = words.get(++pos);
            tvWord.setText(w.getWord().trim());
            setHtmlText(tvUse, String.format("<b>Use in sentence</b><br/>%s", w.getWordUses().trim()));
            setHtmlText(tvMeaning, String.format("<b>Meaning</b><br/>%s", w.getWordMeaning().trim()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setHtmlText(TextView tvUse, String format) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvUse.setText(Html.fromHtml(format, Html.FROM_HTML_MODE_LEGACY));
            }else{
                tvUse.setText(Html.fromHtml(format));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playWord(CharSequence text) {
        if (tts != null){
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, UUID.randomUUID().toString());
            tts.setPitch(1f);
            tts.setSpeechRate(1f);
        }
    }

    List<AddWords> words = new ArrayList<>();
    private void getAllWordsFromFirebase() {
        progressBar.setVisibility(View.VISIBLE);

        CollectionReference ref = FirebaseFirestore.getInstance()
                .collection("learningWords");

        ref.get().addOnCompleteListener(task -> {
            if (task.getResult() == null || task.getResult().isEmpty()){
                progressBar.setVisibility(View.GONE);
                Toaster.longToast("No words found");
                return;
            }
            int size = task.getResult().size();
            int currentItem = 0;
            for (QueryDocumentSnapshot s : task.getResult()) {
                String id = s.getId();
                String wordMeaning =  s.get("wordMeaning") == null ? "":s.get("wordMeaning").toString()  ;
                String wordUse =  s.get("wordUses") == null ? "" : s.get("wordUses").toString();
                words.add(new AddWords(id, wordMeaning, wordUse));
                if (size==++currentItem) {
                    progressBar.setVisibility(View.GONE);
                    rlMain.setVisibility(View.VISIBLE);
                    setValues();
                }
            }
        });

    }


    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.CANADA);
            if (result != TextToSpeech.LANG_MISSING_DATA
                    && result != TextToSpeech.LANG_NOT_SUPPORTED) {


            }
        }
    }
}
