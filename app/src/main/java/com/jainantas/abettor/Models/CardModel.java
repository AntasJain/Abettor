package com.jainantas.abettor.Models;

import android.util.Log;

public class CardModel {
    private String Bank;
    private String Card;
    private String Cvv;
    private String MM;
    private String YY;
    public CardModel() {

    }

    public CardModel(String bank, String card, String cvv, String MM, String YY) {
        Bank = bank;
        Card = card;
        Cvv = cvv;
        this.MM = MM;
        this.YY = YY;
    }

    public String getBank() {
        Log.e("Bank",Bank);
        return Bank;
    }

    public void setBank(String bank) {
        Bank = bank;
    }

    public String getCard() {
        return Card;
    }

    public void setCard(String card) {
        Card = card;
    }

    public String getCvv() {
        return Cvv;
    }

    public void setCvv(String cvv) {
        Cvv = cvv;
    }

    public String getMM() {
        return MM;
    }

    public void setMM(String MM) {
        this.MM = MM;
    }

    public String getYY() {
        return YY;
    }

    public void setYY(String YY) {
        this.YY = YY;
    }
}
