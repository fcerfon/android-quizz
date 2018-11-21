package om.superquizz.diginamic.superquizz.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import om.superquizz.diginamic.superquizz.R;

public class ScoreFragment extends Fragment {
    private static final String SCORE = "score";
    private static final String MAX_SCORE = "maxScore";

    private String score;
    private String maxScore;

    public ScoreFragment() {
        // Required empty public constructor
    }

    // Conservé pour référence (non utilisé pour ce fragment)
    public static ScoreFragment newInstance(int param1, int param2) {
        ScoreFragment fragment = new ScoreFragment();
        Bundle args = new Bundle();
        args.putInt(SCORE, param1);
        args.putInt(MAX_SCORE, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            score = getArguments().getString(score);
            maxScore = getArguments().getString(maxScore);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_score, container, false);
    }
}
