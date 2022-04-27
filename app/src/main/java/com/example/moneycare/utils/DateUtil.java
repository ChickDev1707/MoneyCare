package com.example.moneycare.utils;


import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {
    public static String getStringDate(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String sDate = formatter.format(date);
        return sDate;
    }
    //first date of current month
    public static Date getFirstDateOfMonth(){
        LocalDate localDate =  LocalDate.now().withDayOfMonth(1);
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
    public static Date getLastDateOfMonth(){
        LocalDate localDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
