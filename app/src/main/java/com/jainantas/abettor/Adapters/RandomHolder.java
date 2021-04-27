package com.jainantas.abettor.Adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jainantas.abettor.R;

public class RandomHolder extends RecyclerView.ViewHolder {
    private TextView DateRandom;
    private TextView Values;
    public RandomHolder(@NonNull View itemView) {
        super(itemView);
        DateRandom=itemView.findViewById(R.id.dateRandomSugar);
        Values=itemView.findViewById(R.id.randomSugar);
    }
    public void setDateRandom(String dateRandom)
    {
        DateRandom.setText(dateRandom);
    }
    public void setValues(String values)
    {
        Values.setText(values);
    }
}
