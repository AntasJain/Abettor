package com.jainantas.abettor.Models;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class BPModel {
    private String Date;
    private String Low;
    private String High;

    public BPModel() {
    }

    public BPModel(String date, String low, String high) {
        Date = date;
        Low = low;
        High = high;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }

    public String getDate() {
        return Date;
    }

    public String getLow() {
        return Low;
    }

    public String getHigh() {
        return High;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setHigh(String high) {
        High = high;
    }

    public void setLow(String low) {
        Low = low;
    }

}
