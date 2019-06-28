package com.example.cracksanalysis;

public class Model2 {
    private int id;
    private int id_bangunan;
    private int stuktur;
    private int level;

    public Model2 (int id, int id_bangunan, int struktur, int level){
        this.id=id;
        this.id_bangunan=id_bangunan;
        this.stuktur=struktur;
        this.level=level;
    }

    public int getStuktur() {
        return stuktur;
    }

    public void setStuktur(int stuktur) {
        this.stuktur = stuktur;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_bangunan() {
        return id_bangunan;
    }

    public void setId_bangunan(int id_bangunan) {
        this.id_bangunan = id_bangunan;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
