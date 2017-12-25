package com.example.bismeet.technoquiz;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
        String id= String.valueOf(getSharedPreferences(Config.Shared_ID_PREF,MODE_PRIVATE).getInt(Config.KEY_ID,0));
        getScore(id);



    }

    private void getScore(final String id) {
        StringRequest scoreRequest=new StringRequest(Request.Method.POST, Config.SCORE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response",response);
                try {
                    parseScore(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error",error.getMessage());

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> scoremap=new HashMap<>();
                scoremap.put(Config.KEY_ID,id);
                return scoremap;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(scoreRequest);
    }

    private void parseScore(String response) throws JSONException {

        JSONObject jsonObject=new JSONObject(response);
        JSONArray jsonArray=jsonObject.getJSONArray("results");
        JSONObject jsonObject1=jsonArray.getJSONObject(0);
        int right=jsonObject1.getInt("right");
        int wrong=jsonObject1.getInt("wrong");
        int total=jsonObject1.getInt("net");
        textViewscore.setText("Right Answers:"+right+"\n"+"Wrong Answers:"+wrong+"\n"+"Total Score:"+"\n"
        +total+"\n");


    }
}
