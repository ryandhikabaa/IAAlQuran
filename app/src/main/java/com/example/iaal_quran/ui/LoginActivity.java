package com.example.iaal_quran.ui;

import android.content.Intent;
import android.content.SharedPreferences;
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

/**
 *Created : Ryandhika Bintang Abiyyi Kudus
 *don't try to reupload my project, thanks from me
 */

public class LoginActivity extends AppCompatActivity {

    TextView tdate,ttime,nameapps,infoapps;
    EditText etemail, etpassword;
    Button button_signin;
    String text_email, text_pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tdate = findViewById(R.id.date);
        ttime = findViewById(R.id.time);
        etemail = findViewById(R.id.et_email);
        etpassword =  findViewById(R.id.et_pw);
        button_signin =  findViewById(R.id.btn_lg);
        nameapps =  findViewById(R.id.nameapps);
        infoapps =  findViewById(R.id.infoapps);

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
                Intent goLogin = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(goLogin);
                finish();
            }
        });

        button_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text_email = etemail.getText().toString();
                text_pw = etpassword.getText().toString();
                if (text_email.length()==0) {
                    etemail.setError("Enter email");
                } else if (text_pw.length()==0) {
                    etpassword.setError("Enter password");
                }else {
                    SharedPreferences sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", etemail.getText().toString());
                    editor.putString("pw", etpassword.getText().toString());
                    editor.commit();
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }
}
