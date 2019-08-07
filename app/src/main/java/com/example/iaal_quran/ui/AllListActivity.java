package com.example.iaal_quran.ui;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class AllListActivity extends AppCompatActivity {

    private static final String TAG = AllListActivity.class.getSimpleName();
    private RecyclerView rvSurah;
    private RecyclerViewAdapter surahAdapter;
    private List<Surah> surahList = new ArrayList<>();
    private ProgressDialog mProgress;
    SwipeRefreshLayout swipeLayout;
    private Realm realm;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        rvSurah = findViewById(R.id.recyclerView);
        swipeLayout = findViewById(R.id.swipe_container);

        //Set up Realm
        Realm.init(AllListActivity.this);
        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        realm = Realm.getInstance(configuration);

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Processing...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        if (realm == null)
            realm = Realm.getDefaultInstance();

        mProgress.show();
        fetchCustDataFromDb();


        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code here
                surahList.clear();
                fetchCustDataFromDb();
                // To keep animation for 4 seconds
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Stop animation (This will be after 3 seconds)
                        swipeLayout.setRefreshing(false);
                    }
                }, 1500);
                Toast.makeText(getApplicationContext(), "Surah is Up to date!", Toast.LENGTH_SHORT).show();// Delay in millis
            }
        });

        setupRecyclerSurah();
    }

    private void setupRecyclerSurah() {
        surahAdapter = new RecyclerViewAdapter(this, surahList);
        rvSurah.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvSurah.setHasFixedSize(true);
        rvSurah.setAdapter(surahAdapter);
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
                                /** save to realm */

                                surahList.add(item);
                            }
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm _realm) {
                                    _realm.insertOrUpdate(surahList);
                                }
                            });
                            mProgress.dismiss();
                            surahAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("", "onError: " + anError.getErrorBody());
                        Toast.makeText(AllListActivity.this, Constants.EROR, Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void fetchCustDataFromDb() {
        surahList.clear();
        try {
            RealmResults<Surah> surah = realm.where(Surah.class)
//                    .equalTo("nama", "Al baqaroh")

                    .findAll();
            if (surah.size() <= 0) {
                fetchSurahApi();
                Log.e(TAG, "fetchCustDataFromApi: ");
            } else {
                Log.e(TAG, "fetchCustDataFromDb: " + surah.size());
                this.surahList.addAll(surah);
                mProgress.dismiss();
                this.surahAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e(TAG, "fetchCustDataFromDb: " + e.getLocalizedMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
