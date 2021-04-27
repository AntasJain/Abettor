package com.jainantas.abettor.Models;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class MedicineTTModel {
    private String StartDate;
    private String EndDate;
    private String Medicine;
    private String Description;
    private String Days;
    private String Times;
    public MedicineTTModel(){

    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }

    public String getTimes() {
        return Times;
    }

    public void setTimes(String times) {
        Times = times;
    }

    public MedicineTTModel(String startDate, String endDate, String medicine, String description, String days, String times)
    {
        StartDate=startDate;
        EndDate=endDate;
        Medicine=medicine;
        Description=description;
        Days=days;
        Times=times;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;

    }

    public String getEndDate() {
        Log.e("ERRRRR","ERRRRRRRRRRRRRR");
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getMedicine() {
        return Medicine;
    }

    public void setMedicine(String medicine) {
        Medicine = medicine;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDays() {
        return Days;
    }

    public void setDays(String days) {
        Days = days;
    }
}
