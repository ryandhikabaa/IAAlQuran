package com.example.iaal_quran.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.iaal_quran.R;

import java.text.SimpleDateFormat;

public class LoginActivity extends AppCompatActivity {

    TextView tdate,ttime,nameapps,infoapps;
    EditText etusername, etpassword;
    Button button_signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tdate = (TextView) findViewById(R.id.date);
        ttime = (TextView) findViewById(R.id.time);
        etusername = (EditText) findViewById(R.id.et_email);
        etpassword = (EditText) findViewById(R.id.et_pw);
        button_signin = (Button) findViewById(R.id.btn_lg);
        nameapps = (TextView) findViewById(R.id.nameapps);
        infoapps = (TextView) findViewById(R.id.infoapps);

        Animation a = AnimationUtils.loadAnimation(this,R.anim.mytransition);
        tdate.setAnimation(a);
        ttime.setAnimation(a);
        nameapps.setAnimation(a);
        infoapps.setAnimation(a);

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

        TextView tvSignup = findViewById(R.id.tv_signup);
        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goLogin = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(goLogin);
                finish();
            }
        });


    }
}
