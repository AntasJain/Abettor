package com.jainantas.abettor.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jainantas.abettor.Adapters.SlideAdapter;
import com.jainantas.abettor.Fragments.FinanceFragment;
import com.jainantas.abettor.Fragments.HealthFragment;
import com.jainantas.abettor.Fragments.HomeFragment;
import com.jainantas.abettor.Preferences.PrefsData;
import com.jainantas.abettor.Preferences.SharedPreferencesHelper;
import com.jainantas.abettor.R;

import java.util.HashMap;
import java.util.concurrent.Executor;

public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private HomeFragment homeFragment;
    private FinanceFragment financeFragment;
    private HealthFragment healthFragment;
    ViewPager viewPager;
    MenuItem prev;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACTIVITY_RECOGNITION
            },100);
        }
        FirebaseAuth auth=FirebaseAuth.getInstance();
        FirebaseUser user=auth.getCurrentUser();

//        mClient= GoogleSignIn.getClient(this,options);
        SharedPreferencesHelper.init(this);
        SharedPreferencesHelper.setUserInfo(PrefsData.picLink,user.getPhotoUrl().toString());
        SharedPreferencesHelper.setUserInfo(PrefsData.emailId,user.getEmail());
        SharedPreferencesHelper.setUserInfo(PrefsData.userName,user.getDisplayName());
        int n=getApplicationContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if(n==Configuration.UI_MODE_NIGHT_YES)
            findViewById(R.id.relative).setBackgroundColor(Color.BLACK);
        viewPager=findViewById(R.id.viewPager);
        bottomNavigationView=findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.profileHome:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.btnFinances:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.btnHealth:
                        viewPager.setCurrentItem(2);
                        return true;
                }
                return false;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(prev!=null)
                    prev.setChecked(false);
                else
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prev=bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupViewPager(viewPager);
        //Toast.makeText(this,SharedPreferencesHelper.getBiometric(PrefsData.fingerprintAuth,null),Toast.LENGTH_LONG).show();
    }
    public void setupViewPager(ViewPager viewPager){
        SlideAdapter adapter=new SlideAdapter(getSupportFragmentManager());
        homeFragment=new HomeFragment();
        financeFragment=new FinanceFragment();
        healthFragment=new HealthFragment();
        adapter.addFragment(homeFragment);
        adapter.addFragment(financeFragment);
        adapter.addFragment(healthFragment);
        viewPager.setAdapter(adapter);
    }
}
/*
        if (SharedPreferencesHelper.getBiometric(PrefsData.fingerprintAuth, null) == null)
            dialogForBiometricAuth();
        else setUpInitializer();
    }

    public void dialogForBiometricAuth() {
        AlertDialog.Builder fpBuilder = new AlertDialog.Builder(this);
        fpBuilder.setTitle("Secure Your Data with Biometrics.").setMessage("Add FingerPrint Authentication?\nYou can Always change preference in the settings.");
        fpBuilder.setPositiveButton("Sure!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferencesHelper.setBiometric(PrefsData.fingerprintAuth, PrefsData.authDone);
                // Toast.makeText(HomeActivity.this,"Fingerprint enabled"+ SharedPreferencesHelper.getBiometric(PrefsData.fingerprintAuth,null),Toast.LENGTH_LONG).show();
                dialog.dismiss();
                setUpInitializer();
            }
        });
        fpBuilder.setNegativeButton("No!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferencesHelper.setBiometric(PrefsData.fingerprintAuth, PrefsData.authDont);
                //   Toast.makeText(HomeActivity.this,"Fingerprint disabled",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                setUpInitializer();
            }
        });
        AlertDialog alertDialog = fpBuilder.create();
        fpBuilder.setCancelable(false);
        fpBuilder.show();


    }

    public void setUpInitializer() {

        if (SharedPreferencesHelper.getBiometric(PrefsData.fingerprintAuth, PrefsData.authSet).equals(PrefsData.authDont))
            setupView();
        else if (SharedPreferencesHelper.getBiometric(PrefsData.fingerprintAuth, PrefsData.authSet).equals(PrefsData.authDone))
            biometricStartUp();
    }

    public void biometricStartUp() {
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                executeBiometrics();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                alertForBiometrics(1);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                alertForBiometrics(2);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                alertForBiometrics(3);


        }
    }

    public void alertForBiometrics(int response) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error !");
        if (response == 1)
            builder.setMessage("Sensor is not Available");
        else if (response == 2)
            builder.setMessage("Fingerprint Sensor not found");
        else if (response == 3)
            builder.setMessage("No Fingerprints found, Please add One or More FIngerPrints to the device");
        builder.setPositiveButton("Okay!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void executeBiometrics(){
        Executor executor= ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt=new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                setupView();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();

            }
        });
        BiometricPrompt.PromptInfo promptInfo=new BiometricPrompt.PromptInfo.Builder().setTitle("Authenticate")
                .setDescription("Place Your Finger on the Sensor").setNegativeButtonText("Use Pin").build();
        biometricPrompt.authenticate(promptInfo);

    }
    public void setupView(){
    Toast.makeText(this,"Setting View",Toast.LENGTH_LONG).show();}
}*/
