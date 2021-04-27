package com.jainantas.abettor.Models;

import android.util.Log;

public class BankModel {
    String Bank;
    String Account;
    String Ifsc;

    public BankModel(String bank, String account, String ifsc) {
        Bank = bank;
        Account = account;
        Ifsc = ifsc;
    }
    public BankModel(){}

    public String getBank() {
        return Bank;
    }

    public void setBank(String bank) {
        Bank = bank;
    }

    public String getAccount() {
        return Account;
    }

    public void setAccount(String account) {
        Account = account;
    }

    public String getIfsc() {

        Log.e("Reached Here","Till IFSC");
        return Ifsc;
    }

    public void setIfsc(String ifsc) {
        Ifsc = ifsc;
    }
}
