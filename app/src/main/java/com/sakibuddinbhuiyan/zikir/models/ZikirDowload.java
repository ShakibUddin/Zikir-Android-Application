package com.sakibuddinbhuiyan.zikir.models;

public class ZikirDowload {
    private String english;
    private String bangla;

    public ZikirDowload() {

    }

    public ZikirDowload(String english, String bangla) {
        this.english = english;
        this.bangla = bangla;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getBangla() {
        return bangla;
    }

    public void setBangla(String bangla) {
        this.bangla = bangla;
    }
}
