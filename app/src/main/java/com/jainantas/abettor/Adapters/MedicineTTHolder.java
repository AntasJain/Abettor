package com.jainantas.abettor.Adapters;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jainantas.abettor.R;

public class MedicineTTHolder extends RecyclerView.ViewHolder {
    private View view;
    public MedicineTTHolder(@NonNull View itemView) {
        super(itemView);
        view=itemView;
        Log.e("ERR","HELLO ERROR");
    }
    public void setStartDate(String date)
    {
        TextView start=view.findViewById(R.id.startDateIp);
        start.setText("From: "+date);
    }
    public void setEndDate(String endDate)
    {
        TextView end=view.findViewById(R.id.endDateIp);
        end.setText("To: "+endDate);
    }
    public void setMedicine(String medicine){
        TextView med=view.findViewById(R.id.medName);
        med.setText(medicine);
    }
    public void setDescription(String description)
    {
        TextView desc=view.findViewById(R.id.description);
        if(!description.isEmpty())
        desc.setText("Description:\t"+description);
        else
            desc.setText("Description:\tNot Provided");
    }
    public void setTimes(String times)
    {
        TextView time=view.findViewById(R.id.timesmed);
        if(!times.isEmpty())
        time.setText("x"+times+" Times.");
    }
    public void setDays(String days)
    {
        TextView day=view.findViewById(R.id.days);

        if(days.isEmpty()||days.length()==0)
            day.setText("Weekly");
        else { days=days.trim();
            days=days.replace(' ',',')+'.';
            day.setText(days);
        }
    }


}
