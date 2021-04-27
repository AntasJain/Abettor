package com.jainantas.abettor.Adapters;

import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.recyclerview.widget.RecyclerView;

import com.jainantas.abettor.Preferences.PrefsData;
import com.jainantas.abettor.Preferences.SharedPreferencesHelper;
import com.jainantas.abettor.R;
import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;
import java.util.concurrent.Executor;

public class CardHolder extends RecyclerView.ViewHolder {
    View view;

    public CardHolder(@NonNull View itemView) {
        super(itemView);
        view=itemView;


    }
    public void setBank(String bank){
        Log.e("Bank",bank);
        TextView bnk=view.findViewById(R.id.bankName);
        bnk.setText(bank);
    }
    public void setCard(String card){
        TextView crd=view.findViewById(R.id.CardNumber);
        String hyphenized="";
        hyphenized=card.substring(0,4)+"-"+card.substring(4,8)+"-"+card.substring(8,12)+"-"+card.substring(12,card.length());
        crd.setText(hyphenized);
    }
    public void setCvv(String cvv){
        TextView myCvv=view.findViewById(R.id.CVV);
        //myCvv.setText(cvv);
        try{
            myCvv.setText(AESCrypt.decrypt(SharedPreferencesHelper.getUserInfo(PrefsData.emailId,null),cvv));
        }
        catch (GeneralSecurityException e){
            e.printStackTrace();
        }

    }
    public void setMM(String mm){
        TextView mon=view.findViewById(R.id.validity);
        mon.setText(mm+"/");

    }
    public void setYY(String yy){
        TextView yr=view.findViewById(R.id.validity);
        yr.setText(yr.getText().toString()+yy);
    }

}
