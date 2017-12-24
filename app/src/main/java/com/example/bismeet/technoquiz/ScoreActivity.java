package com.example.bismeet.technoquiz;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by bismeet on 24/12/17.
 */

public class ScoreActivity extends AppCompatActivity {
    private TextView textViewscore;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_layout);
        textViewscore=findViewById(R.id.team_score);


    }
}
