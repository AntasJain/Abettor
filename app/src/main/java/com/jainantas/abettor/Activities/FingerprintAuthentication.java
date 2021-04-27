package com.jainantas.abettor.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricFragment;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.jainantas.abettor.R;

import java.util.concurrent.Executor;

public class FingerprintAuthentication extends AppCompatActivity {
    Executor executor;
    BiometricPrompt prompt;
    BiometricPrompt.PromptInfo promptInfo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_authenticate);
       authenticate();

    }
    public void authenticate(){
        executor= ContextCompat.getMainExecutor(this);
        prompt=new BiometricPrompt(FingerprintAuthentication.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),errString,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Intent intent=new Intent(FingerprintAuthentication.this,HomeActivity.class);
                finish();startActivity(intent);finishAffinity();

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                finish();
            }
        });
        promptInfo=new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Auhentication")
                .setSubtitle("Login Using FIngerprint Authntication")
                .setNegativeButtonText("Cancel Login")
                .build();
        prompt.authenticate(promptInfo);
    }
}
