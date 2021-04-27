package com.jainantas.abettor.Activities;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jainantas.abettor.Preferences.PrefsData;
import com.jainantas.abettor.Preferences.SharedPreferencesHelper;
import com.jainantas.abettor.R;

import java.util.Calendar;

public class Alerts extends AppCompatActivity {
EditText title,location,description;
Button addEvent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert);
        title=findViewById(R.id.titleAlert);
        location=findViewById(R.id.locationAlert);
        description=findViewById(R.id.descAlert);
        addEvent=findViewById(R.id.buttonAlert);
        SharedPreferencesHelper.init(this);
        location.setText(SharedPreferencesHelper.getUserInfo(PrefsData.City,null));
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!title.getText().toString().isEmpty() && !location.getText().toString().isEmpty() && !description.getText().toString().isEmpty())
                {
                    Intent intent=new Intent(Intent.ACTION_INSERT);
                    intent.setData(CalendarContract.Events.CONTENT_URI);
                    intent.putExtra(CalendarContract.Events.TITLE,title.getText().toString());
                    intent.putExtra(CalendarContract.Events.EVENT_LOCATION,location.getText().toString());
                    intent.putExtra(CalendarContract.Events.DESCRIPTION,description.getText().toString());
                    intent.putExtra(CalendarContract.Events.ALL_DAY,"true");
                    startActivity(intent);
                    /*if(intent.resolveActivity(getPackageManager())!=null)
                    {

                    }
                    else {
                        Toast.makeText(Alerts.this,"Action not supported",Toast.LENGTH_LONG).show();
                    }*/
                }
                else {
                    Toast.makeText(Alerts.this,"All Fields are Necessary",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
