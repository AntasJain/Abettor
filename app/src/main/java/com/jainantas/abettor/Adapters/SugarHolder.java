package com.jainantas.abettor.Adapters;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jainantas.abettor.R;

public class SugarHolder extends RecyclerView.ViewHolder {
    private final TextView DateRecord;
    private final TextView Fasting;
    private final TextView PostPrandial;

    public SugarHolder(@NonNull View itemView) {
        super(itemView);
        DateRecord=itemView.findViewById(R.id.dateRecordSugar);
        Fasting=itemView.findViewById(R.id.fasting);
        PostPrandial=itemView.findViewById(R.id.postPrandial);
    }
    public void setDateRecord(String date){
        Log.e("Task","Reached Holder");
        DateRecord.setText(date);
    }
    public void setFasting(String fasting){
        Fasting.setText(fasting);
    }
    public void setPostPrandial(String postprandial)
    {
        PostPrandial.setText(postprandial);
    }
}
