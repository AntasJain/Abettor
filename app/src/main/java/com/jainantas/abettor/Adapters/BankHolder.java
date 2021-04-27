package com.jainantas.abettor.Adapters;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jainantas.abettor.Preferences.PrefsData;
import com.jainantas.abettor.Preferences.SharedPreferencesHelper;
import com.jainantas.abettor.R;
import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;

public class BankHolder extends RecyclerView.ViewHolder {
    View view;
    public BankHolder(@NonNull View itemView) {
        super(itemView);
        view=itemView;
    }
    public void setBank(String Bank)
    {
        TextView bank=view.findViewById(R.id.banknameview);
        bank.setText(Bank);
    }
    public void setAccount(String Account){
        TextView acc=view.findViewById(R.id.accountNum_item);
        try {
            acc.setText("Account: "+AESCrypt.decrypt(SharedPreferencesHelper.getUserInfo(PrefsData.emailId,null),Account));
        }
        catch (GeneralSecurityException e)
        {
            Toast.makeText(view.getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    public void setIfsc(String IFSC){
        TextView ifsc=view.findViewById(R.id.ifsc_item);
        ifsc.setText("IFSC: "+IFSC);
    }

}
