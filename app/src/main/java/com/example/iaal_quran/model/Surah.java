package com.example.iaal_quran.model;

import android.os.Parcel;
import android.os.Parcelable;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Surah extends RealmObject implements Parcelable{
    private String nomor;
    private String nama;
    private String asma;
    private String name;
    private String start;
    private String ayat;
    private String type;
    private String urut;
    private String rukuk;
    private String arti;
    private String keterangan;

    public Surah() {
    }

    public Surah(String nomor, String nama, String asma, String name, String start, String ayat, String type, String urut, String rukuk, String arti, String keterangan) {
        this.nomor = nomor;
        this.nama = nama;
        this.asma = asma;
        this.name = name;
        this.start = start;
        this.ayat = ayat;
        this.type = type;
        this.urut = urut;
        this.rukuk = rukuk;
        this.arti = arti;
        this.keterangan = keterangan;
    }

    protected Surah(Parcel in) {
        nomor = in.readString();
        nama = in.readString();
        asma = in.readString();
        name = in.readString();
        start = in.readString();
        ayat = in.readString();
        type = in.readString();
        urut = in.readString();
        rukuk = in.readString();
        arti = in.readString();
        keterangan = in.readString();
    }

    public static final Creator<Surah> CREATOR = new Creator<Surah>() {
        @Override
        public Surah createFromParcel(Parcel in) {
            return new Surah(in);
        }

        @Override
        public Surah[] newArray(int size) {
            return new Surah[size];
        }
    };

    public String getNomor() {
        return nomor;
    }

    public String getNama() {
        return nama;
    }

    public String getAsma() {
        return asma;
    }

    public String getName() {
        return name;
    }

    public String getStart() {
        return start;
    }

    public String getAyat() {
        return ayat;
    }

    public String getType() {
        return type;
    }

    public String getUrut() {
        return urut;
    }

    public String getRukuk() {
        return rukuk;
    }

    public String getArti() {
        return arti;
    }

    public String getKeterangan() {
        return keterangan;
    }

    //Setter

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setAsma(String asma) {
        this.asma = asma;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setAyat(String ayat) {
        this.ayat = ayat;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUrut(String urut) {
        this.urut = urut;
    }

    public void setRukuk(String rukuk) {
        this.rukuk = rukuk;
    }

    public void setArti(String arti) {
        this.arti = arti;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nomor);
        parcel.writeString(nama);
        parcel.writeString(asma);
        parcel.writeString(name);
        parcel.writeString(start);
        parcel.writeString(ayat);
        parcel.writeString(type);
        parcel.writeString(urut);
        parcel.writeString(rukuk);
        parcel.writeString(arti);
        parcel.writeString(keterangan);
    }
}
