package com.example.moneycare.utils;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.preference.PreferenceManager;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    private static Calendar calendar = Calendar.getInstance();
    public static String getDateString(Context context, Date date){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String value = sharedPreferences.getString("date_formats", "dd/MM/yyyy");

        SimpleDateFormat formatter = new SimpleDateFormat(String.format("EE, %s", value));
        String sDate = formatter.format(date);
        return sDate;
    }
    public static String getMonthString(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("MMM, yyyy");
        String sDate = formatter.format(date);
        return sDate;
    }
    public static String getYearString(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("YYYY");
        String sDate = formatter.format(date);
        return sDate;
    }
    public static int getDay(Date date){
        calendar.setTime(date);
        int day = calendar.get(Calendar.DATE);
        return day;
    }
    public static int getYear(Date date){
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        return year;
    }
    public static int getMonth(Date date){
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH);
        return month;
    }
    public static boolean compareDate(Date first, Date second){
        int firstDay = getDay(first);
        int firstMonth = getMonth(first);
        int firstYear = getYear(first);

        int secondDay = getDay(second);
        int secondMonth = getMonth(second);
        int secondYear = getYear(second);
        if(firstDay == secondDay && firstMonth == secondMonth && firstYear == secondYear) return true;
        else return false;
    }
    public static boolean compareMonth(Date first, Date second){
        int firstMonth = getMonth(first);
        int firstYear = getYear(first);

        int secondMonth = getMonth(second);
        int secondYear = getYear(second);
        if(firstMonth == secondMonth && firstYear == secondYear) return true;
        else return false;
    }
    public static boolean compareYear(Date first, Date second){
        int firstYear = getYear(first);
        int secondYear = getYear(second);
        if(firstYear == secondYear) return true;
        else return false;
    }
    public static Date createDate(int day, int month, int year){
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, day);
        Date date = calendar.getTime();
        return date;
    }
    public static String getFullTimeString(int hour, int minute){
        String hourString = Integer.toString(hour);
        String minuteString = Integer.toString(minute);
        hourString = hourString.length() == 1? "0" + hourString: hourString;
        minuteString = minuteString.length() == 1? "0" + minuteString: minuteString;
        return hourString + ":" + minuteString;
    }

}
