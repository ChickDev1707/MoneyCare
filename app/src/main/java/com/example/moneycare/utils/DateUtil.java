package com.example.moneycare.utils;


import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String getStringDate(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String sDate = formatter.format(date);
        return sDate;
    }
}
