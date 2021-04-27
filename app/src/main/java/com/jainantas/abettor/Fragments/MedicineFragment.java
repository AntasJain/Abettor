package com.jainantas.abettor.Fragments;

import android.app.DatePickerDialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.icu.number.Scale;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.jainantas.abettor.Preferences.PrefsData;
import com.jainantas.abettor.Preferences.SharedPreferencesHelper;
import com.jainantas.abettor.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MedicineFragment extends Fragment {
    LinearLayout layout;
    SwitchCompat switch1;
    SwitchMaterial mon,tue,wed,thurs,fri,sat,sun;
    ImageView i1,i2;
    Button save;
    FirebaseFirestore db;
    EditText startDate,endDate,medicine,times,desc;
    String date,day="";
    private int mYear, mMonth, mDay;
    HashMap<String,Object> medMap;
    public MedicineFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_frame_medicine,container,false);
        switch1=rootView.findViewById(R.id.switch1);
        layout=rootView.findViewById(R.id.checkLayout);
        mon=rootView.findViewById(R.id.mon);
        tue=rootView.findViewById(R.id.tues);
        wed=rootView.findViewById(R.id.wed);
        thurs=rootView.findViewById(R.id.thu);
        fri=rootView.findViewById(R.id.fri);
        sat=rootView.findViewById(R.id.sat);
        sun=rootView.findViewById(R.id.sun);
        startDate=rootView.findViewById(R.id.startDate);
        endDate=rootView.findViewById(R.id.endDate);
        medicine=rootView.findViewById(R.id.med_name);
        times=rootView.findViewById(R.id.times);
        save=rootView.findViewById(R.id.save);
        desc=rootView.findViewById(R.id.desc);
        i1=rootView.findViewById(R.id.imageView);
        i2=rootView.findViewById(R.id.imageView2);
        switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switch1.isChecked())
                    layout.setVisibility(View.VISIBLE);
                else
                    layout.setVisibility(View.GONE);
            }
        });
        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar(v);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mon.isChecked())
                    day+="Mon ";
                if (tue.isChecked())
                    day+="Tue ";
                if(wed.isChecked())
                    day+="Wed ";
                if(thurs.isChecked())
                    day+="Thu ";
                if(fri.isChecked())
                    day+="Fri ";
                if(sat.isChecked())
                    day+="Sat ";
                if(sun.isChecked())
                    day+="Sun ";
               attemptQuery();
            }
        });
        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar(v);
            }
        });




        return rootView;
    }
    public void calendar(View v)
    {
        final Calendar calendar=Calendar.getInstance();
        mYear=calendar.get(Calendar.YEAR);
        mMonth=calendar.get(Calendar.MONTH);
        mDay=calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
             date=Integer.toString(dayOfMonth)+"-"+Integer.toString(month+1)+"-"+Integer.toString(year);
             if(v==i1)
                 startDate.setText(date);
             else if(v==i2)
                 endDate.setText(date);
            }
        },mYear,mMonth,mDay);
        datePickerDialog.show();


    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        medMap=new HashMap<>();
        db=FirebaseFirestore.getInstance();
    }
    public void attemptQuery()
    {
        String start,end,med,des,time;
        start=startDate.getText().toString();
        end=endDate.getText().toString();
        med=medicine.getText().toString();
        des=desc.getText().toString();
        time=times.getText().toString();
        medMap.put("StartDate",start);
        medMap.put("EndDate",end);
        medMap.put("Medicine",med);
        medMap.put("Description",des);
        medMap.put("Times",time);
        medMap.put("Days",day);
        String Date = new SimpleDateFormat("d-MM-yyyy, HH:mm:ss").format(Calendar.getInstance().getTime());
        db.collection(SharedPreferencesHelper.getUserInfo(PrefsData.emailId,null))
                .document("Health").collection("TimeTable")
                .document(Date).set(medMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(),"Added",Toast.LENGTH_SHORT).show();
                setBack();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Failed",Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void setBack(){
        endDate.setText("");
        endDate.clearFocus();
        startDate.setText("");
        startDate.clearFocus();
        times.setText("");
        times.clearFocus();
        desc.setText("");
        desc.clearFocus();
        medicine.clearFocus();
        medicine.setText("");
        switch1.setChecked(false);
        layout.setVisibility(View.GONE);
        mon.setChecked(false);
        tue.setChecked(false);
        wed.setChecked(false);
        thurs.setChecked(false);
        fri.setChecked(false);
        sat.setChecked(false);
        sun.setChecked(false);
    }

}
