package com.jainantas.abettor.Adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.ServerTimestamp;
import com.jainantas.abettor.R;

import java.util.Date;

public class BPHolder extends RecyclerView.ViewHolder {
    private View view;

    public BPHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }

    public void setDate(String date) {
        TextView date_ = view.findViewById(R.id.date);
        date_.setText(date);
    }

    public void setHigh(String high) {
        TextView highbp = view.findViewById(R.id.highBP);
        highbp.setText(high);
    }

    public void setLow(String low) {
        TextView lowbp = view.findViewById(R.id.lowBP);
        lowbp.setText(low);
    }



}
