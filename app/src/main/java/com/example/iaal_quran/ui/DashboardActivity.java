package com.example.iaal_quran.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.iaal_quran.Constants;
import com.example.iaal_quran.R;
import com.example.iaal_quran.adapter.RecyclerViewAdapter;
import com.example.iaal_quran.model.Surah;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class DashboardActivity extends AppCompatActivity {
    TextView tdate,ttime,tvUsername;
    private SharedPreferences preferences;
    private ImageView ivLogout;
    private CardView c_mekah, c_madinah, c_all_surah;
    private static final int TIME_LIMIT = 1500;
    private static long backPressed;
    private RecyclerViewAdapter surahAdapter;
    private List<Surah> surahList = new ArrayList<>();
    private Realm realm;
    private static final String TAG = AllListActivity.class.getSimpleName();
    /**
     *Created : Ryandhika Bintang Abiyyi Kudus
     *don't try to reupload my project, thanks from me
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Set up Realm
        Realm.init(DashboardActivity.this);
        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        realm = Realm.getInstance(configuration);

        if (realm == null)
            realm = Realm.getDefaultInstance();

        if (!isConnected(DashboardActivity.this)){
            RealmResults<Surah> surah = realm.where(Surah.class)
                    .findAll();
            if (surah.size() <= 0) {
                buildDialogrealnull(DashboardActivity.this).show();
            } else {
                buildDialogDatadone(DashboardActivity.this).show();
            }
        }
        else {
            Toast.makeText(DashboardActivity.this,"Welcome", Toast.LENGTH_SHORT).show();
            fetchCustDataFromDb();
            setContentView(R.layout.activity_dashboard);
        }

        tdate = findViewById(R.id.date);
        ttime = findViewById(R.id.time);
        tvUsername = findViewById(R.id.tv_username);
        ivLogout = findViewById(R.id.iv_logout);
        c_mekah = findViewById(R.id.card_mekah);
        c_madinah = findViewById(R.id.card_madinah);
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
                                surahList.clear();
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
        c_mekah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, MekahActivity.class);
                startActivity(intent);

            }
        });
        c_madinah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, MadinahActivity.class);
                startActivity(intent);

            }
        });
    }


    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
        else return false;
        } else
        return false;
    }

    public AlertDialog.Builder buildDialogDatadone(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("Anda akan akses data offline");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return builder;
    }

    public AlertDialog.Builder buildDialogrealnull(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("Data offline belum diunduh, pastikan anda terkoneksi dengan internet");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return builder;
    }

    @Override
    public void onBackPressed() {
        if (TIME_LIMIT + backPressed > System.currentTimeMillis()){
            super.onBackPressed();;
        }else {
            Toast.makeText(getApplicationContext(),"Tekan lagi untuk keluar",Toast.LENGTH_SHORT).show();
        }
        backPressed =System.currentTimeMillis();
    }

    private void fetchSurahApi() {
        AndroidNetworking.get(Constants.BASE_URL)
                .setTag("hasil")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray hasilList = response.getJSONArray("hasil");
                            for (int i = 0; i < hasilList.length(); i++) {
                                JSONObject hasil = hasilList.getJSONObject(i);
                                Surah item = new Surah();
                                item.setNomor(hasil.getString("nomor"));
                                item.setNama(hasil.getString("nama"));
                                item.setAsma(hasil.getString("asma"));
                                item.setAyat(hasil.getString("ayat"));
                                item.setType(hasil.getString("type"));
                                item.setArti(hasil.getString("arti"));
                                item.setUrut(hasil.getString("urut"));
                                item.setKeterangan(hasil.getString("keterangan"));
                                surahList.add(item);
                            }
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm _realm) {
                                    _realm.insertOrUpdate(surahList);
                                }
                            });
                            surahAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("", "onError: " + anError.getErrorBody());
                        Toast.makeText(DashboardActivity.this, Constants.EROR_DATAREALM_NULL, Toast.LENGTH_SHORT).show();
                        buildDialogrealnull(DashboardActivity.this).show();
                    }
                });


    }

    private void fetchCustDataFromDb() {
        surahList.clear();
        try {
            RealmResults<Surah> surah = realm.where(Surah.class)
                    .findAll();
            if (surah.size() <= 0) {
                fetchSurahApi();
                Log.e(TAG, "fetchCustDataFromApi: ");
            } else {
                Log.e(TAG, "fetchCustDataFromDb: " + surah.size());
                this.surahList.addAll(surah);
                this.surahAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e(TAG, "fetchCustDataFromDb: " + e.getLocalizedMessage());
        }
    }


    public void delete(){
        final RealmResults<Surah> model = realm.where(Surah.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                model.deleteFromRealm(0);
            }
        });
    }
}
