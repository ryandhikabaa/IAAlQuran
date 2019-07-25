package com.example.iaal_quran.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.iaal_quran.R;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView rvJobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        rvJobs = findViewById(R.id.recyclerView );
    }
}
