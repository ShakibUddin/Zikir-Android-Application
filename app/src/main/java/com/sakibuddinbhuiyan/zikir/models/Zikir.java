package com.sakibuddinbhuiyan.zikir.models;

public class Zikir {
    public String zikir;
    public String zikirBangla;
    public Integer readToday;
    public Integer readTotal;
    public int favourite;

    public Zikir(String zikir, String zikirBangla, Integer readToday, Integer readTotal, int favourite) {
        this.zikir = zikir;
        this.zikirBangla = zikirBangla;
        this.readToday = readToday;
        this.readTotal = readTotal;
        this.favourite = favourite;
    }
}
