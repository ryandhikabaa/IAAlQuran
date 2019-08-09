package com.example.iaal_quran.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.iaal_quran.R;

import java.text.SimpleDateFormat;

public class SplashActivity extends AppCompatActivity {

    TextView tdate,ttime,nameapps,infoapps;
    Animation frombottom, fromtop;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tdate = (TextView) findViewById(R.id.date);
        ttime = (TextView) findViewById(R.id.time);
        nameapps = (TextView) findViewById(R.id.nameapps);
        infoapps = (TextView) findViewById(R.id.infoapps);

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Processing...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);


        Animation animation = AnimationUtils.loadAnimation(this,R.anim.fromtop);
        tdate.setAnimation(animation);
        ttime.setAnimation(animation);
        nameapps.setAnimation(animation);
        infoapps.setAnimation(animation);



        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                long date = System.currentTimeMillis();
                                long time = System.currentTimeMillis();
                                SimpleDateFormat sdf_time = new SimpleDateFormat("hh-mm-ss a");
                                String timeString = sdf_time.format(time);
                                ttime.setText(timeString);

                                SimpleDateFormat sdf_date = new SimpleDateFormat("MMM dd yyyy");
                                String dateString = sdf_date.format(date);
                                tdate.setText(dateString);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);

                String username = prefs.getString("email", "");
                String name = prefs.getString("user", "");
                if (username == ""){
                    //login
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    //mainmenu

                    Intent intent1 = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(intent1);
                    finish();
                }

            }
        },2000);
    }
}
