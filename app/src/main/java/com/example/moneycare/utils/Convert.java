package com.example.moneycare.utils;

public class Convert {
    public static Long convertToNumber(String str){
        try {
            String[] arrs = str.split(",");
            return Long.valueOf(String.join("", arrs));
        }
        catch(Exception e){
            return -1L;
        }
    }
}
