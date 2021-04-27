package com.jainantas.abettor.Fragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jainantas.abettor.Activities.BloodPressureTracker;
import com.jainantas.abettor.Activities.BloodSugarTracker;

import com.jainantas.abettor.Activities.Prescriptions;
import com.jainantas.abettor.Activities.Reports;
import com.jainantas.abettor.Activities.TimetableActivity;
import com.jainantas.abettor.Preferences.PrefsData;
import com.jainantas.abettor.Preferences.SharedPreferencesHelper;
import com.jainantas.abettor.R;
import com.jainantas.abettor.Services.StepCounter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class HealthFragment extends Fragment {
    TextView weather, temp, city, up,down,date,name,fb,ppb,datebs;
    ImageView imageView;
    SwipeRefreshLayout swipeRefreshLayout;
    CardView bp,bs,bmi,tt,reports,presc;
    FirebaseFirestore db;
    TextView stepcnt;
    //
    String countedStep="Counter";
    String detectedStep="Detector";
    boolean stopped;
    private Intent serviceIntent;

    //
    public HealthFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serviceIntent=new Intent(getActivity(), StepCounter.class);
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_health, container, false);
        db=FirebaseFirestore.getInstance();
        name=rootView.findViewById(R.id.setName);
        name.setText("Hello!\n"+SharedPreferencesHelper.getUserInfo(PrefsData.userName,null));
        up=rootView.findViewById(R.id.upBP);
        down=rootView.findViewById(R.id.downBP);
        stepcnt=rootView.findViewById(R.id.stepCounted);
        presc=rootView.findViewById(R.id.prescribed);
        date=rootView.findViewById(R.id.bpDate);
        fb=rootView.findViewById(R.id.bs1);
        ppb=rootView.findViewById(R.id.bs2);
        datebs=rootView.findViewById(R.id.bsDate);
        temp = rootView.findViewById(R.id.temp);
        weather = rootView.findViewById(R.id.weather);
        imageView=rootView.findViewById(R.id.iconWeather);
        city = rootView.findViewById(R.id.city);
        swipeRefreshLayout = rootView.findViewById(R.id.swipe);
        bp=rootView.findViewById(R.id.bpDetails);
        bs=rootView.findViewById(R.id.bsDetails);
        bmi=rootView.findViewById(R.id.bmi_calculator);
        tt=rootView.findViewById(R.id.timeTable);
        reports=rootView.findViewById(R.id.report);
        getWeather(); setBP(); setBS();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getWeather(); setBP();

                swipeRefreshLayout.setRefreshing(false);
            }
        });
        city.setText(SharedPreferencesHelper.getWeather("city","City"));
        //String ico=SharedPreferencesHelper.getWeather("icon","empty");
//        if(!ico.equals("empty"))
        //Glide.with(getContext()).load(ico).into(imageView);
        weather.setText(SharedPreferencesHelper.getWeather("desc","Description"));
        temp.setText(SharedPreferencesHelper.getWeather("temp","00°C"));
        stepcnt.setText(String.valueOf(SharedPreferencesHelper.getSteps("Steps",0)));

        //getWeather();
        bp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), BloodPressureTracker.class);
                startActivity(intent);
            }
        });
        bs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), BloodSugarTracker.class);
                startActivity(intent);
            }
        });
        bmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateBMI();
            }
        });
        tt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), TimetableActivity.class);
                startActivity(intent);
            }
        });
        presc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Prescriptions.class);
                startActivity(intent);
            }
        });
        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Reports.class);
                startActivity(intent);
            }
        });
        return rootView;
    }

    public void getWeather() {
        String url = "http://api.openweathermap.org/data/2.5/weather?q=Mainpuri&appid=b661a44c7a415cdce1a09129b7aca671";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject mainobj = response.getJSONObject("main");
                    JSONArray array = response.getJSONArray("weather");
                    JSONObject object = array.getJSONObject(0);
                    String temperature = String.valueOf(mainobj.getDouble("temp"));
                    String description = object.getString("description");
                    String city_ = response.getString("name");
                    String icon=    object.getString("icon");
                    Log.e("icon",icon);
                    Log.e("Temp", temperature);
            //        Toast.makeText(getContext(), city_, Toast.LENGTH_LONG).show();
                    Log.e("City", city_);
                    temp.setText(toCelsius(temperature));
                    weather.setText(description);

                    city.setText(city_);
                    String uri="http://openweathermap.org/img/w/" + icon +".png";
                    SharedPreferencesHelper.setWeather("icon",uri);
                    SharedPreferencesHelper.setWeather("temp",toCelsius(temperature));
                    SharedPreferencesHelper.setWeather("city",city_);
                    SharedPreferencesHelper.setWeather("desc",description);
                    Glide.with(getContext()).load(uri).into(imageView);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                    temp.setText(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // temp.setText(error.toString());
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();

            }
        });
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }

    public String toCelsius(String temp) {
        double k = Double.parseDouble(temp);
        double c = k - 273.15;
        DecimalFormat dformat = new DecimalFormat();
        dformat.setMaximumFractionDigits(1);
        return dformat.format(c) + "°C";
    }
    public void setBP()
    {
        Query query=db.collection(SharedPreferencesHelper
                .getUserInfo(PrefsData.emailId,null))
                .document("Health").collection("BP").orderBy("Date", Query.Direction.DESCENDING).limit(1);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        down.setText("Down "+document.get("Low").toString());
                        up.setText("Up "+document.get("High").toString());
                        date.setText("Last Updated On: "+ document.get("Date").toString());
                    }
                } else {
                    Toast.makeText(getContext(), "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    public void setBS() {
        Query query = db.collection(SharedPreferencesHelper.getUserInfo(PrefsData.emailId, null))
                .document("Health").collection("BSN").orderBy("DateRecord", Query.Direction.DESCENDING).limit(1);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document :task.getResult()){
                        fb.setText("FBS "+document.get("Fasting").toString());
                        ppb.setText("PPBS "+document.get("PostPrandial").toString());
                        datebs.setText("Last Updated On: "+document.get("DateRecord").toString());
                    }

                }
                else {
                    Toast.makeText(getContext(),"Error getting documents "+task.getException(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void calculateBMI()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        View custom =getLayoutInflater().inflate(R.layout.activity_bmi,null);
        builder.setView(custom);
        EditText wt=custom.findViewById(R.id.weight);
        EditText ht=custom.findViewById(R.id.height);
        Button calc=custom.findViewById(R.id.bntCalc);
        TextView op= custom.findViewById(R.id.output);
        Button clear= custom.findViewById(R.id.clear_data);
        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wt.getText().toString().length()>0 && wt.getText().toString().length()>0) {
                   String status=new String();
                    double weight = Double.parseDouble(wt.getText().toString());
                    double height = Double.parseDouble(ht.getText().toString()) / 100;
                    double bmi = weight / Math.pow(height, 2);
                    if (bmi < 18.5)
                        status = "Underweight";
                    else if (bmi <= 24.9)
                        status = "Normal Weight";
                    else if (bmi < 30)
                        status = "Overweight";
                    else if (bmi < 35)
                        status = "Obese Class 1";
                    else if (bmi < 40)
                        status = "Obese Class 2";
                    else
                        status = "Obese Class 3";

                    op.setText("BMI: " + Double.toString(bmi) + "\nStatus: " + status);
                    op.setTextColor(Color.parseColor("#00ffff"));
                }
                else {
                    op.setText("Please Enter Required alues");
                    op.setTextColor(Color.parseColor("#ff0000"));
                }
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wt.setText("");
                ht.setText("");
                op.setText("");
                ht.setFocusable(false);
                wt.setFocusable(false);
            }
        });
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
       alertDialog.getWindow().setBackgroundDrawableResource(R.color.fui_transparent);
        alertDialog.show();
    }
    public void init(){
        stopped=true;
        getActivity().startService(new Intent(getContext(),StepCounter.class));
        getActivity().registerReceiver(receiver,new IntentFilter(StepCounter.BROADCAST_ACTION));
        stopped=false;

        /*
        if(!stopped){
        unregisterReceiver(receiver);
        stopService(new Intent(getContext(),StepCounter.class));
        stopped=true;
        }
         */
    }
    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateSteps(intent);
            Log.e("Reached OnReceive","--------------------");
        }
    };
    private void updateSteps(Intent intent){
        countedStep=intent.getStringExtra("Counted_Step");
        detectedStep=intent.getStringExtra("Detected_Step");


        stepcnt.setText(countedStep);
        SharedPreferencesHelper.setSteps("Steps",Integer.parseInt(countedStep));

    }

}