package com.jainantas.abettor.Models;

import android.renderscript.Sampler;

public class RandomModel {
    String DateRandom;
    String Values;

    public RandomModel(){}

    public RandomModel(String dateRandom, String values)
    {
        DateRandom=dateRandom;
        Values=values;
    }

    public String getDateRandom() {
        return DateRandom;
    }

    public void setDateRandom(String dateRandom) {
        DateRandom = dateRandom;
    }

    public String getValues() {
        return Values;
    }

    public void setValues(String values) {
        Values = values;
    }
}
