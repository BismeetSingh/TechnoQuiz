package com.example.bismeet.technoquiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
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
        final ProgressDialog progressDialog=ProgressDialog.show(LoginActivity.this,"Please wait","Loading");
        progressDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Config.LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if(response.equalsIgnoreCase("Login Successful"))
                {
                    startActivity(new Intent(LoginActivity.this,Question.class));

                }
                else
                {
                    Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> login=new HashMap<>();
                login.put(Config.KEY_ID,teamid.getText().toString());
                return login;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

}
