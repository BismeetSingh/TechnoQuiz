package com.example.bismeet.technoquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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

public class Registration_Two_member extends AppCompatActivity {
    private EditText name, email, phone;
    private int teamid;
    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_member_two_main);
        name = findViewById(R.id.second_member_name);
        email = findViewById(R.id.Email_second);
        phone = findViewById(R.id.phone_second);
        teamid=getIntent().getIntExtra("id",0);
        awesomeValidation=new AwesomeValidation(ValidationStyle.BASIC);
        Button next = findViewById(R.id.register_button_two);
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
        awesomeValidation.addValidation(Registration_Two_member.this, R.id.second_member_name, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(Registration_Two_member.this, R.id.Email_second, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        awesomeValidation.addValidation(Registration_Two_member.this, R.id.phone_second, "^[7-9][0-9]{9}$", R.string.phoneerror);


        return awesomeValidation.validate();

    }

    private void savetodb() {
        final String memname = name.getText().toString();
        final String mememail = email.getText().toString();
        final String memphone = phone.getText().toString();
       final StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.MEMBER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equalsIgnoreCase("success"))
                {
                    Toast.makeText(getApplicationContext(),"You are successfully registered",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Registration_Two_member.this,Question.class));
                }
                else {
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Registration_Two_member.this,"Error",Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> regtwo = new HashMap<>();
                regtwo.put(Config.KEY_MEMBER_NAME, memname);
                regtwo.put(Config.KEY_MEMBER_EMAIL, mememail);
                regtwo.put(Config.KEY_MEMBER_PHONE, memphone);
                regtwo.put("id",String.valueOf(teamid));
                return regtwo;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }
}
