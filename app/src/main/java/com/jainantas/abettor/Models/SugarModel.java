package com.jainantas.abettor.Models;

import android.util.Log;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class SugarModel {
    private String DateRecord;
    private String Fasting;
    private String PostPrandial;
    private Date mTimestamp;
    public SugarModel(){}

    public SugarModel(String dateRecord,String fasting, String postPrandial)
    {
        DateRecord=dateRecord;
        Fasting=fasting;
        PostPrandial=postPrandial;
    }
    public String getDateRecord(){
        return DateRecord;
    }
    public String getFasting(){
        return Fasting;
    }
    public String getPostPrandial(){
        return PostPrandial;
    }
    public void setDateRecord(String dateRecord)
    {
        DateRecord=dateRecord;
    }

    public void setFasting(String fasting) {
        Fasting = fasting;
    }
    public void setPostPrandial(String postPrandial)
    {
        PostPrandial=postPrandial;
    }
    @ServerTimestamp
    public Date getTimestamp(){
        return mTimestamp;
    }
    public  void setTimestamp(Date timestamp)
    {
        mTimestamp=timestamp;
    }

}
