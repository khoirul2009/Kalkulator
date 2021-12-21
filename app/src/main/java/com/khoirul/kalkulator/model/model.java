package com.khoirul.kalkulator.model;

import com.google.firebase.Timestamp;

import java.util.Date;

public class model {

    private Timestamp date;
    private String id, angka1, operasi, angka2, hasil;

    public model(  String angka1, String operasi, String angka2, String hasil) {
        this.date = date;
        this.angka1 = angka1;
        this.operasi = operasi;
        this.angka2 = angka2;
        this.hasil = hasil;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }


    public String getAngka1() {
        return angka1;
    }

    public void setAngka1(String angka1) {
        this.angka1 = angka1;
    }

    public String getOperasi() {
        return operasi;
    }

    public void setOperasi(String operasi) {
        this.operasi = operasi;
    }

    public String getAngka2() {
        return angka2;
    }

    public void setAngka2(String angka2) {
        this.angka2 = angka2;
    }

    public String getHasil() {
        return hasil;
    }

    public void setHasil(String hasil) {
        this.hasil = hasil;
    }
}
