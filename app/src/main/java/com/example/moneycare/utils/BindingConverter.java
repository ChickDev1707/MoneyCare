package com.example.moneycare.utils;

import androidx.databinding.InverseMethod;

public class BindingConverter {
    @InverseMethod("stringToLong")
    public static String longToString(Long number) {
        return Long.toString(number);
    }

    public static Long stringToLong(String numString) {
        if(numString.equals("")) return 0L;
        else return Long.parseLong(numString);
    }
}
