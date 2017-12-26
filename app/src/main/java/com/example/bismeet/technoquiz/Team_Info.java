package com.example.bismeet.technoquiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
 * Created by bismeet on 19/12/17.
 */

public class Team_Info extends AppCompatActivity{
    private EditText team_name,college_name;
    private Spinner house_vjti;

    @Override
    protected void onResume() {
        super.onResume();
        if(getSharedPreferences(Config.Shared_ID_PREF,MODE_PRIVATE).getInt(Config.KEY_ID,0)!=0)
        {
            startActivity(new Intent(Team_Info.this,LoginActivity.class));
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_main);
        team_name=findViewById(R.id.team_name);
        college_name=findViewById(R.id.college_name);
        house_vjti= findViewById(R.id.house_spinner);
        final Button next_welcome = findViewById(R.id.next_button_to_member);
        next_welcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveandgotomemberdetails();
            }
        });

    }

    private void saveandgotomemberdetails() {
        final  String name=team_name.getText().toString();
        final String college=college_name.getText().toString();
        if(TextUtils.isEmpty(name)|| TextUtils.isEmpty(college))
        {
            Toast.makeText(this, "Please fill all appropriate values", Toast.LENGTH_SHORT).show();
        }
        else {
            savetodb();
        }
    }

    private void savetodb() {

        final String name=team_name.getText().toString();
        final String college=college_name.getText().toString();
        final String house=house_vjti.getSelectedItem().toString();
        final ProgressDialog progressDialog=ProgressDialog.show(Team_Info.this,"Loading","Please Wait");
        progressDialog.show();
        final StringRequest teamdbreq=new StringRequest(Request.Method.POST, Config.TEAM_COLLEGE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if(response.equalsIgnoreCase("Insertion Failed"))
                {
                    Toast.makeText(Team_Info.this, "Team Already Exists", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Team_Info.this,LoginActivity.class));
                }
                else {
                    final  int id=Integer.parseInt(response);
                    if(id>0)
                    {


                        final SharedPreferences sharedPreferences=getSharedPreferences(Config.Shared_ID_PREF,MODE_PRIVATE);
                        final SharedPreferences.Editor editor=sharedPreferences.edit().putInt(Config.KEY_ID,id);
                        editor.commit();

                        startActivity(new Intent(Team_Info.this,Registration_One_Member.class).putExtra("id",id));
                    }
                    else {
                        Toast.makeText(Team_Info.this,"Error",Toast.LENGTH_SHORT).show();
                    }
                }






            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Team_Info.this,"Error",Toast.LENGTH_SHORT).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                final Map<String,String> team_map=new HashMap<>();
                team_map.put(Config.KEY_COLLEGE,college);
                team_map.put(Config.KEY_HOUSE,house);
                team_map.put(Config.KEY_TEAM,name);
                return team_map;

            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(teamdbreq);

    }
}
