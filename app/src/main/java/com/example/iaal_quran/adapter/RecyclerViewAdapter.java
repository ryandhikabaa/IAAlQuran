package com.example.iaal_quran.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.iaal_quran.R;
import com.example.iaal_quran.model.Surah;
import com.example.iaal_quran.ui.DetailActivity;

import java.util.List;

public class RecyclerViewAdapter extends  RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Surah> mData;

    public RecyclerViewAdapter(Context mContext, List<Surah> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_list_surat, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Surah surah = mData.get(position);

        holder.tv_nomor.setText(surah.getNomor());//
        holder.tv_nama.setText(surah.getNama());//
        holder.tv_arti.setText(surah.getArti());//
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_nomor , tv_nama, tv_asma, tv_name, tv_start, tv_ayat, tv_type, tv_urut, tv_arti, tv_keterangan;

        MyViewHolder(View itemView) {
            super(itemView);
            tv_nomor = itemView.findViewById(R.id.tv_nomor);
            tv_nama = itemView.findViewById(R.id.tv_surat);
            tv_arti = itemView.findViewById(R.id.tv_arti);
            tv_asma = itemView.findViewById(R.id.tv_asma);
            tv_ayat = itemView.findViewById(R.id.tv_jumlah_ayat);
            tv_type = itemView.findViewById(R.id.tv_turun_surat);
            tv_urut = itemView.findViewById(R.id.tv_urutanwahyu);
            tv_keterangan = itemView.findViewById(R.id.tv_keterangan);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext, DetailActivity.class);
                    i.putExtra("hasil", mData.get(getAdapterPosition()));
                    mContext.startActivity(i);
                }
            });
        }
    }
}
