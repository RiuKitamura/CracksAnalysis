package com.example.cracksanalysis;

public class Model {
    private int id;
    private String nama_b;
    private String lantai;
    private String thn;
    private String alamat_b;
    private String lati;
    private String longi;
    private byte[] poto;
    private String nama;
    private String alamat;
    private String hp;

    public Model(int id, String nama_b, String lantai, String thn, String alamat_b, String lati,
                 String longi, byte[] poto, String nama, String alamat, String hp){
        this.id = id;
        this.nama_b = nama_b;
        this.lantai = lantai;
        this.thn = thn;
        this.alamat_b = alamat_b;
        this.lati = lati;
        this.longi = longi;
        this.poto = poto;
        this.nama = nama;
        this.alamat = alamat;
        this.hp = hp;
    }
    public Model(int id, String nama_b, String lantai, String thn, String alamat_b, String lati,
                 String longi, String nama, String alamat, String hp){
        this.id = id;
        this.nama_b = nama_b;
        this.lantai = lantai;
        this.thn = thn;
        this.alamat_b = alamat_b;
        this.lati = lati;
        this.longi = longi;
        this.nama = nama;
        this.alamat = alamat;
        this.hp = hp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama_b() {
        return nama_b;
    }

    public void setNama_b(String nama_b) {
        this.nama_b = nama_b;
    }

    public String getLantai() {
        return lantai;
    }

    public void setLantai(String lantai) {
        this.lantai = lantai;
    }

    public String getThn() {
        return thn;
    }

    public void setThn(String thn) {
        this.thn = thn;
    }

    public String getAlamat_b() {
        return alamat_b;
    }

    public void setAlamat_b(String alamat_b) {
        this.alamat_b = alamat_b;
    }

    public String getLati() {
        return lati;
    }

    public void setLati(String lati) {
        this.lati = lati;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public byte[] getPoto() {
        return poto;
    }

    public void setPoto(byte[] poto) {
        this.poto = poto;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }
}
