package com.example.bismeet.technoquiz;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;

/**
 * Created by bismeet on 24/12/17.
 */

public class LoginActivity extends AppCompatActivity {
    private EditText teamid;
    private Button submit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        teamid=findViewById(R.id.teamid);
        submit=findViewById(R.id.sendteamid);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                senddetails();
            }
        });
    }

    private void senddetails() {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

}
