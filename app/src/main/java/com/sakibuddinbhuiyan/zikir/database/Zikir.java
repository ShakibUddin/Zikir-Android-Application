package com.sakibuddinbhuiyan.zikir.database;

public class Zikir {
    public String zikir;
    public Integer readToday;
    public Integer readTotal;
    public int favourite;

    public Zikir(String zikir, Integer readToday, Integer readTotal, int favourite) {
        this.zikir = zikir;
        this.readToday = readToday;
        this.readTotal = readTotal;
        this.favourite = favourite;
    }
}
