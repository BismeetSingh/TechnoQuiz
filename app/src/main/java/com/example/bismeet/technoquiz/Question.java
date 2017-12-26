package com.example.bismeet.technoquiz;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * Created by bismeet on 19/12/17.
 */

public class Question extends AppCompatActivity {
    private ImageView quesimage;
    private Date timeone, timetwo;
    private TextView timer;
    private RadioGroup optionsgroup;
    private int quesid;
    private RadioButton one, two, three, four;
    private TextView questionTextView;
    private boolean flag = true;
    private CountDownTimer countDownTimer;
    private int count = 0;
    private int time;
    private int questioncount = 20;
    private int optionidone, optionidtwo, optionidthree, optionidfour;
    int arr[][] = {{0, 0, 2}, {0, 1, 3}, {0, 2, 3}, {1, 0, 2}, {1, 1, 3}, {1, 2, 3}, {2, 0, 1}, {2, 1, 1}, {2, 2, 2}};//, {2, 0, 1}, {2, 1, 1}, {2, 2, 2}, {3, 0, 1}, {3, 1, 1}, {3, 2, 1}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_layout);
        quesimage = findViewById(R.id.quesimage);
        timer = findViewById(R.id.timer);
        questionTextView = findViewById(R.id.questext);
        optionsgroup = findViewById(R.id.optionsgroup);
        one = findViewById(R.id.optionone);
        two = findViewById(R.id.optiontwo);
        three = findViewById(R.id.optionthree);
        four = findViewById(R.id.optionfour);
        final Button nextquestion = findViewById(R.id.nextquestion);
        rand();
        nextquestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                submitoptionSelected();
                rand();


            }
        });
        Log.d("times", "" + time);
//        String string1 = "22:00:00";
//        Date time1 = null;
//        try {
//            time1 = new SimpleDateFormat("HH:mm:ss").parse(string1);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        Calendar calendar1 = Calendar.getInstance();
//        calendar1.setTime(time1);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String formattedtime = df.format(c.getTime());
        try {
            timeone = df.parse(formattedtime);
            Log.d("time1",""+timeone.getTime());
        } catch (ParseException e) {
            Log.d("error", e.getMessage());
        }
        Log.d("time", formattedtime);
        String[] time = formattedtime.split(":");
        Log.d("timetest", time[0] + " " + time[1]);
//        int hour=calendar1.get(Calendar.HOUR_OF_DAY);


//

    }

    private void submitoptionSelected() {
        int optid = 0;
        final int teamid = getSharedPreferences(Config.Shared_ID_PREF, MODE_PRIVATE).getInt(Config.KEY_ID, 0);
        Log.d("teamid", "" + teamid);
        if (one.isChecked()) {
            optid = optionidone;
        } else if (two.isChecked()) {
            optid = optionidtwo;
        } else if (three.isChecked()) {
            optid = optionidthree;
        } else if (four.isChecked()) {
            optid = optionidfour;
        }
        final int finalOptid = optid;
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SUBMIT_OPTIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("Success") || response.equalsIgnoreCase("Updated succesfully")) {
                    optionsgroup.clearCheck();

//                    if(!flag)
//                    {
                    while (!flag) {
                        flag = true;
                        Log.d("randcall", "I am being called from options");
                        rand();
                    }
//                    rand();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "" + error.getMessage());

            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> selectedmap = new HashMap<>();
                selectedmap.put(Config.KEY_ID, String.valueOf(teamid));
                Log.d("ids", "" + teamid + "  " + quesid);
                selectedmap.put(Config.KEY_QUESTION_ID, String.valueOf(quesid));
                selectedmap.put(Config.KEY_OPTION_ID, String.valueOf(finalOptid));
                return selectedmap;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }


    private void getImage(final int id, final String sendtype, final String sendlvl) {
        final ProgressDialog progressDialog = ProgressDialog.show(Question.this, "PLease wait", "Loading");
        progressDialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.IMAGE_QUESTIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.d("image", response);
                try {
                    parseQuestionsResponse(response);
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errorimage", "" + error.getMessage());
                progressDialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> map = new HashMap<>();
                map.put(Config.KEY_ID, String.valueOf(id));
                map.put(Config.KEY_TYPE, sendtype);
                map.put(Config.KEY_LEVEL, sendlvl);
                Log.d("values", "" + id + sendtype + sendlvl);
                return map;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);


    }

    private void parseQuestionsResponse(String response) throws JSONException {
        final JSONObject jsonObject = new JSONObject(response);
        final JSONArray array = jsonObject.getJSONArray("questions");
        int randnum;
        int count = 0;
        count++;
        Log.d("callcount", "" + count + " " + " " + quesid + " " + array.length());


        if (array.length() == 0) {

            rand();
        } else {
            randnum = new Random().nextInt(array.length());
            JSONObject quesobj = array.getJSONObject(randnum);
            Log.d("old", "" + quesid);
            Log.d("old1", "" + quesobj.getInt("QuesId"));

            quesid = quesobj.getInt("QuesId");
            Log.d("quesid", "" + quesid);
            String ques = quesobj.getString("Question");
            String purl = quesobj.getString("PhotoURL");
            time = quesobj.getInt("Time");
//            Log.d("time", "" + time);
            questionTextView.setText(ques);
            if (!purl.equalsIgnoreCase("")) {
                quesimage.setVisibility(View.VISIBLE);
                quesimage.setImageResource(getResources().getIdentifier("drawable/" + purl, null, getPackageName()));

            } else {
                quesimage.setVisibility(View.GONE);
            }
            getOptions();
        }

    }


    private void getOptions() {
        final ProgressDialog progressDialog = ProgressDialog.show(Question.this, "Loading", "Please wait");
        progressDialog.show();
        final Request.Priority priority = Request.Priority.HIGH;
        final StringRequest optionsRequest = new StringRequest(Request.Method.POST, Config.OPTIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    parseOptions(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "" + error.getMessage());
                progressDialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> optionsmap = new HashMap<>();
                optionsmap.put(Config.KEY_ID, String.valueOf(quesid));
                return optionsmap;
            }

            @Override
            public Priority getPriority() {
                return priority;
            }

        };
        Volley.newRequestQueue(getApplicationContext()).add(optionsRequest);
    }

    private void parseOptions(String response) throws JSONException {
        final JSONObject optionsobj = new JSONObject(response);
        final JSONArray jsonArray = optionsobj.getJSONArray("questions");
        final JSONObject option1 = jsonArray.getJSONObject(0);
        final JSONObject option2 = jsonArray.getJSONObject(1);
        final JSONObject option3 = jsonArray.getJSONObject(2);
        final JSONObject option4 = jsonArray.getJSONObject(3);
        one.setText(option1.getString("question"));
        two.setText(option2.getString("question"));
        three.setText(option3.getString("question"));
        four.setText(option4.getString("question"));
        optionidone = option1.getInt("optionid");
        optionidtwo = option2.getInt("optionid");
        optionidthree = option3.getInt("optionid");
        optionidfour = option4.getInt("optionid");
        count++;
        Log.d("fetch", "" + time);
        reverseTimer(time, timer);

    }


    public void rand() {


        final Random rand = new Random();
        if (count == questioncount) {

//            Log.d("done","Questions done");
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("HH:mm");
            String formattedtime = df.format(c.getTime());
            try {
                timetwo = df.parse(formattedtime);
            } catch (ParseException e) {
                Log.d("error", e.getMessage());
            }
            Log.d("times", "" + timetwo.getTime());
//            String[] time = formattedtime.split(":");
//            Log.d("timetest", time[0] + " " + time[1]);
            long mills = Math.abs(timeone.getTime() - timetwo.getTime());
            senddiff(mills);
//           Log.d("millis",String.valueOf(mills));
            return;

        }
//

        final int qtype = rand.nextInt(3);
        final int qlevel = rand.nextInt(3);
//        int qlevel = 0;
//        int qtype = 0;
        final String[] type = {"LQ", "CS", "IB"};
        final String[] level = {"Easy", "Medium", "Hard"};
        String sendtype;
        String sendlvl;
        switch (qtype) {
            case 0:
                switch (qlevel) {
                    case 0: {
                        if (arr[0][2] > 0) {
                            sendtype = type[qtype];
                            sendlvl = level[qlevel];
                            arr[0][2]--;
                            getImage(getSharedPreferences(Config.Shared_ID_PREF, MODE_PRIVATE).getInt(Config.KEY_ID, 0), sendtype, sendlvl);
                            break;
                        } else {
                            flag = false;
                            return;
                        }

                    }

                    case 1: {
                        if (arr[1][2] > 0) {
                            sendtype = type[qtype];
                            sendlvl = level[qlevel];
                            arr[1][2]--;
                            getImage(getSharedPreferences(Config.Shared_ID_PREF, MODE_PRIVATE).getInt(Config.KEY_ID, 0), sendtype, sendlvl);
                            break;
                        } else {
                            flag = false;
                            return;
                        }

                    }

                    case 2:

                    {
                        if (arr[2][2] > 0) {
                            sendtype = type[qtype];
                            sendlvl = level[qlevel];
                            arr[2][2]--;
                            getImage(getSharedPreferences(Config.Shared_ID_PREF, MODE_PRIVATE).getInt(Config.KEY_ID, 0), sendtype, sendlvl);
                            break;
                        } else {
                            flag = false;
                            return;
                        }

                    }
                }
                break;
            case 1:
                switch (qlevel) {
                    case 0: {
                        if (arr[3][2] > 0) {
                            sendtype = type[qtype];
                            sendlvl = level[qlevel];
                            arr[3][2]--;
                            getImage(getSharedPreferences(Config.Shared_ID_PREF, MODE_PRIVATE).getInt(Config.KEY_ID, 0), sendtype, sendlvl);
                            break;
                        } else {
                            flag = false;
                            return;
                        }

                    }

                    case 1:

                    {
                        if (arr[4][2] > 0) {
                            sendtype = type[qtype];
                            sendlvl = level[qlevel];
                            arr[4][2]--;
                            getImage(getSharedPreferences(Config.Shared_ID_PREF, MODE_PRIVATE).getInt(Config.KEY_ID, 0), sendtype, sendlvl);
                            break;
                        } else {
                            flag = false;
                            return;
                        }

                    }
                    case 2:

                    {
                        if (arr[5][2] > 0) {
                            sendtype = type[qtype];
                            sendlvl = level[qlevel];
                            arr[5][2]--;
                            getImage(getSharedPreferences(Config.Shared_ID_PREF, MODE_PRIVATE).getInt(Config.KEY_ID, 0), sendtype, sendlvl);
                            break;
                        } else {
                            flag = false;
                            return;
                        }

                    }

                }
                break;
            case 2:
                switch (qlevel) {
                    case 0:
                        if (arr[6][2] > 0) {
                            sendtype = type[qtype];
                            sendlvl = level[qlevel];
                            arr[6][2]--;
                            getImage(getSharedPreferences(Config.Shared_ID_PREF, MODE_PRIVATE).getInt(Config.KEY_ID, 0), sendtype, sendlvl);
                            break;
                        } else {
                            flag = false;
                            return;
                        }

                    case 1:
                        if (arr[7][2] > 0) {
                            sendtype = type[qtype];
                            sendlvl = level[qlevel];
                            arr[7][2]--;
                            getImage(getSharedPreferences(Config.Shared_ID_PREF, MODE_PRIVATE).getInt(Config.KEY_ID, 0), sendtype, sendlvl);
                            break;
                        } else {
                            flag = false;
                            return;
                        }

                    case 2:
                        if (arr[8][2] > 0) {
                            sendtype = type[qtype];
                            sendlvl = level[qlevel];
                            arr[8][2]--;
                            getImage(getSharedPreferences(Config.Shared_ID_PREF, MODE_PRIVATE).getInt(Config.KEY_ID, 0), sendtype, sendlvl);
                            break;
                        } else {
                            flag = false;
                            return;
                        }


                }
                break;
//            case 3:
//                switch (qlevel) {
//                    case 0:
//                        if (arr[9][2] > 0) {
//                            sendtype = type[qtype];
//                            sendlvl = level[qlevel];
//                            arr[9][2]--;
//                            getImage(getSharedPreferences(Config.Shared_ID_PREF, MODE_PRIVATE).getInt(Config.KEY_ID, 0), sendtype, sendlvl);
//                            break;
//                        } else {
//                            flag = false;
//                            return;
//                        }
//
//                    case 1:
//                        if (arr[10][2] > 0) {
//                            sendtype = type[qtype];
//                            sendlvl = level[qlevel];
//                            arr[10][2]--;
//                            getImage(getSharedPreferences(Config.Shared_ID_PREF, MODE_PRIVATE).getInt(Config.KEY_ID, 0), sendtype, sendlvl);
//                            break;
//                        } else {
//                            flag = false;
//                            return;
//                        }
//
//                    case 2:
//                        if (arr[11][2] > 0) {
//                            sendtype = type[qtype];
//                            sendlvl = level[qlevel];
//                            arr[11][2]--;
//                            getImage(getSharedPreferences(Config.Shared_ID_PREF, MODE_PRIVATE).getInt(Config.KEY_ID, 0), sendtype, sendlvl);
//                            break;
//                        } else {
//                            flag = false;
//                            return;
//                        }
//
//
//                }
//                break;


        }


    }

    private void senddiff(final long mills) {
        final ProgressDialog progressDialog=ProgressDialog.show(Question.this,"Please wait","Loading");
        progressDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Config.TIME_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("responsetime",""+response);
                progressDialog.dismiss();
                if(response.equalsIgnoreCase("Successful"))
                {
                    startActivity(new Intent(Question.this, ScoreActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("time",String.valueOf(mills));
                map.put(Config.KEY_ID,String.valueOf(getSharedPreferences(Config.Shared_ID_PREF,MODE_PRIVATE).getInt(Config.KEY_ID,0)));
                return map;

            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    @Override
    public void onBackPressed() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(Question.this);
        builder.setMessage("Are you sure to exit?");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                System.exit(0);
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void reverseTimer(int Seconds, final TextView tv) {

        Log.d("rand1", "timer called");

        countDownTimer = new CountDownTimer(Seconds * 1000 + 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                tv.setText("TIME : " + String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));
            }

            public void onFinish() {
                submitoptionSelected();
                rand();

            }
        }.start();


    }
}
