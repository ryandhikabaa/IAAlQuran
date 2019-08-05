package com.example.iaal_quran.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.example.iaal_quran.R;
import com.example.iaal_quran.model.Surah;

public class DetailActivity extends AppCompatActivity {
    TextView tv_nomor, tv_NS, tv_asma, tv_arti, tv_JmlAyat,tv_TurunSurat,tv_urutan,tv_keterangan  ;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        toolbar = findViewById(R.id.tl_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detail Surah ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //inisialisasi
        tv_nomor =  findViewById(R.id.tv_nomor);
        tv_NS =  findViewById(R.id.tv_surat);
        tv_asma =  findViewById(R.id.tv_asma);
        tv_arti =  findViewById(R.id.tv_arti);
        tv_JmlAyat =  findViewById(R.id.tv_jumlah_ayat);
        tv_TurunSurat =  findViewById(R.id.tv_turun_surat);
        tv_urutan =  findViewById(R.id.tv_urutanwahyu);
        tv_keterangan =  findViewById(R.id.tv_keterangan);

        final Surah surah = getIntent().getExtras().getParcelable("hasil");

        if(surah != null){
            String nomor = surah.getNomor();
            String nama = surah.getNama();
            String asma = surah.getAsma();
            String arti = surah.getArti();
            String jmlayat = surah.getAyat();
            String turun = surah.getType();
            String urutanwahyu = surah.getUrut();
            String keterangan = String.valueOf(Html.fromHtml(surah.getKeterangan()));

            tv_nomor.setText(nomor);
            tv_NS.setText(nama);
            tv_asma.setText(asma);
            tv_arti.setText(arti);
            tv_JmlAyat.setText(jmlayat);
            tv_TurunSurat.setText(turun);
            tv_urutan.setText(urutanwahyu);
            tv_keterangan.setText(keterangan);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
