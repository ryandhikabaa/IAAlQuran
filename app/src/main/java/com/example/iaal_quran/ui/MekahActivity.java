package com.example.iaal_quran.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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

public class MekahActivity extends AppCompatActivity {
    private static final String TAG = AllListActivity.class.getSimpleName();
    private RecyclerView rvSurah;
    private RecyclerViewAdapter surahAdapter;
    private List<Surah> surahList = new ArrayList<>();
    private ProgressDialog mProgress;
    SwipeRefreshLayout swipeLayout;
    private Realm realm;
    String data;

    /**
     * Created : Ryandhika Bintang Abiyyi Kudus
     * don't try to reupload my project, thanks from me
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mekah);

        rvSurah = findViewById(R.id.recyclerView);
        swipeLayout = findViewById(R.id.swipe_container);

        //Set up Realm
        Realm.init(MekahActivity.this);
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
        mProgress.dismiss();

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                surahList.clear();
                fetchCustDataFromDb();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
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
                        Toast.makeText(MekahActivity.this, Constants.EROR_DATAREALM_NULL, Toast.LENGTH_SHORT).show();
                        buildDialogrealnull(MekahActivity.this).show();
                    }
                });


    }

    private void fetchCustDataFromDb() {
        surahList.clear();
        try {
            RealmResults<Surah> surah = realm.where(Surah.class)
                    .equalTo("type", "mekah")
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

    public AlertDialog.Builder buildDialogrealnull(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("Data offline belum diunduh, pastikan anda terkoneksi dengan internet dan swipe refresh");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return builder;
    }
}
