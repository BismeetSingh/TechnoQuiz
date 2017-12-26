package com.example.bismeet.technoquiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
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
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bismeet on 19/12/17.
 */

public class Registration extends AppCompatActivity {
    private EditText name, email, phone;
    private int teamid;
    AwesomeValidation awesomeValidation;
    private int count = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_member_one_main);
        name = findViewById(R.id.member_name);
        email = findViewById(R.id.Email_member);
        phone = findViewById(R.id.phone_member);
        teamid = getIntent().getIntExtra("id", 0);
        final Button next = findViewById(R.id.member_two_button);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validatefields()) {
                    savetodb();
                }


            }
        });

    }

    private boolean validatefields() {
        awesomeValidation.addValidation(Registration.this, R.id.member_name, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(Registration.this, R.id.Email_member, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        awesomeValidation.addValidation(Registration.this, R.id.phone_member, "^[7-9][0-9]{9}$", R.string.phoneerror);
        return awesomeValidation.validate();


    }

    private void savetodb() {
        final String memname = name.getText().toString();
        final String mememail = email.getText().toString();
        final String memphone = phone.getText().toString();
        final ProgressDialog progressDialog = ProgressDialog.show(Registration.this, "Loading", "Please Wait");
        progressDialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MEMBER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.d("response", response);
                if (response.equalsIgnoreCase("Success")) {
                    count++;
                    name.setText("");
                    email.setText("");
                    phone.setText("");
                    name.setHint("Second Member Name");

                }
                if(count==2)
                {
                    Toast.makeText(getApplicationContext(),"You are successfully registered",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Registration.this,ConfirmationButton.class));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("error", error.getMessage());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> regone = new HashMap<>();
                regone.put(Config.KEY_MEMBER_NAME, memname);
                regone.put(Config.KEY_MEMBER_EMAIL, mememail);
                regone.put(Config.KEY_MEMBER_PHONE, memphone);
                regone.put("id", String.valueOf(teamid));
                return regone;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }
}
