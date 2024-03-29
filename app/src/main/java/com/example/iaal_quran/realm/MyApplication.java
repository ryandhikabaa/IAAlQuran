package com.example.iaal_quran.realm;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;

import io.realm.Realm;
import io.realm.RealmConfiguration;
/**
 *Created : Ryandhika Bintang Abiyyi Kudus
 *don't try to reupload my project, thanks from me
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
        Realm.init(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("data.realm")
                .schemaVersion(2)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

}
