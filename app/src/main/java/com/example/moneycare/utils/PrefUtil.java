package com.example.moneycare.utils;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import com.example.moneycare.data.custom.MoneyFormatter;
import com.google.gson.Gson;

public class PrefUtil {
    public static MoneyFormatter getMoneyFormatter(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("money_format", "");
        MoneyFormatter formatter = new MoneyFormatter();
        if(!json.equals("")) formatter = gson.fromJson(json, MoneyFormatter.class);
        return formatter;
    }
}
