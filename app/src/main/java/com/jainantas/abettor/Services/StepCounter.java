package com.jainantas.abettor.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.jainantas.abettor.Preferences.SharedPreferencesHelper;
import com.jainantas.abettor.R;

public class StepCounter extends Service implements SensorEventListener {
    SensorManager sensorManager;
    Sensor stepCounterSensor;
    Sensor stepDetectorSensor;
    int currentStepsDetected;
    int stepCounter;
    int newStepCounter;
    boolean stopped;
    NotificationManager notificationManager;
    Intent intent;
    int already=0;
    int notifyid=1;

    String CHANNEL_ID="my_Channel";

   /* @Override
    public void onTaskRemoved(Intent rootIntent) {

        super.onTaskRemoved(rootIntent);
        Intent restart=new Intent(getApplicationContext(),this.getClass());
        restart.setPackage(getPackageName());
        startService(rootIntent);
    }*/

    private static final String TAG="StepService";
    public static final String BROADCAST_ACTION="com.jainantas.abettor.bradcast";
    private final Handler handler=new Handler();
    int counter=0;

    @Override
    public void onCreate() {
        super.onCreate();
        intent=new Intent(BROADCAST_ACTION);
        SharedPreferencesHelper.init(this);
        already=SharedPreferencesHelper.getSteps("Steps",0);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //onTaskRemoved(intent);
        Log.v("Service","Start");
        showNotification();
        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        stepCounterSensor=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        stepDetectorSensor=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        sensorManager.registerListener(this,stepCounterSensor,0);
        sensorManager.registerListener(this,stepDetectorSensor,0);
        currentStepsDetected=0;
        stepCounter=0;
        newStepCounter=0;
        stopped=false;
        handler.removeCallbacks(updateBroadcastData);
        // call our handler with or without delay.
        handler.post(updateBroadcastData); // 0 seconds
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("Service", "Stop");

        stopped = true;

        dismissNotification();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_STEP_COUNTER){
            int countstep=(int)event.values[0];
            if(stepCounter==0){
                stepCounter=(int) event.values[0];
            }
            newStepCounter=countstep-stepCounter;
        }
        if(event.sensor.getType()==Sensor.TYPE_STEP_DETECTOR){
            int detect =(int) event.values[0];
            currentStepsDetected+=detect;

        }


        Log.v("Service stepCounter",String.valueOf(newStepCounter));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void showNotification(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name=getString(R.string.channel_name);
            String description =getString(R.string.channel_description);
            int importance=NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel=new NotificationChannel(CHANNEL_ID,name,importance);

        }
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        builder.setContentTitle("Abettor");
        builder.setContentText("Steps stepCounter Running in background");
        builder.setOngoing(true);
        builder.setChannelId(CHANNEL_ID);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,new Intent(),0);
        builder.setContentIntent(pendingIntent);
        notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0,builder.build());
    }
    private void dismissNotification(){
        notificationManager.cancel(0);
    }
    private Runnable updateBroadcastData =new Runnable() {
        @Override
        public void run() {
            if(!stopped){
                broadcastValue();
                //Log.e("iN RUNNABLE","REACHED");
                handler.postDelayed(this,30000);
            }
        }
    };
    private void broadcastValue(){
        Log.d(TAG,"DATA TO ACTIVITY");
        Log.d("Steps",String.valueOf(currentStepsDetected));
        intent.putExtra("Counted_Step_Int", newStepCounter);
        intent.putExtra("Counted_Step", String.valueOf(newStepCounter+already));
        // add step detector to intent.
        intent.putExtra("Detected_Step_Int", currentStepsDetected);
        intent.putExtra("Detected_Step", String.valueOf(currentStepsDetected));
        // call sendBroadcast with that intent  - which sends a message to whoever is registered to receive it.
        sendBroadcast(intent);
    }

}
