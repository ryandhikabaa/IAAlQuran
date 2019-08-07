package com.example.iaal_quran.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iaal_quran.R;
import com.example.iaal_quran.adapter.RecyclerViewAdapter;
import com.example.iaal_quran.model.Surah;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class DashboardActivity extends AppCompatActivity {
    TextView tdate,ttime,tvUsername;
    private SharedPreferences preferences;
    private ImageView ivLogout;
    private CardView c_a, c_b, c_all_surah;
    private static final int TIME_LIMIT = 1500;
    private static long backPressed;
    private RecyclerViewAdapter surahAdapter;
    private List<Surah> surahList = new ArrayList<>();
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tdate = (TextView) findViewById(R.id.date);
        ttime = (TextView) findViewById(R.id.time);
        tvUsername = findViewById(R.id.tv_username);
        ivLogout = findViewById(R.id.iv_logout);
        c_a = findViewById(R.id.card_a);
        c_b = findViewById(R.id.card_b);
        c_all_surah = findViewById(R.id.card_all_surah);

        preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String email = preferences .getString ( "email", "");
        tvUsername.setText (email);

        Animation a = AnimationUtils.loadAnimation(this,R.anim.mytransition);
        tdate.setAnimation(a);
        ttime.setAnimation(a);

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

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DashboardActivity.this)
                        .setMessage("Apakah anda yakin ingin keluar ?")
                        .setNegativeButton("Tidak", null)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.clear();
                                editor.apply();
                                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }).create().show();
            }
        });

        c_all_surah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, AllListActivity.class);
                startActivity(intent);

            }
        });
    }

//    public void delete(Integer id){
//        final RealmResults<Surah> model = realm.where(Surah.class).equalTo("id", id).findAll();
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                model.deleteFromRealm(0);
//            }
//        });
//    }



    @Override
    public void onBackPressed() {
        if (TIME_LIMIT + backPressed > System.currentTimeMillis()){
            super.onBackPressed();;
        }else {
            Toast.makeText(getApplicationContext(),"Tekan lagi untuk keluar",Toast.LENGTH_SHORT).show();
        }
        backPressed =System.currentTimeMillis();
    }
}
