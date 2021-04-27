package com.jainantas.abettor.Models;

import androidx.annotation.NonNull;

public class ExpenseModel {


    private String Date;
    private String Description;
    private Long INR;

    public ExpenseModel() {

    }
    public ExpenseModel(String date, String description, Long INR) {
        Date = date;
        Description = description;
        this.INR = INR;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Long getINR() {
        return INR;
    }

    public void setINR(Long INR) {
        this.INR = INR;
    }
}
