package com.example.moneycare.utils;

import android.view.View;
import android.widget.EditText;
public class ValidationUtil {

    public static boolean checkEmpty(EditText view){
        if (view.getText().toString().equals("")) {
            view.setError("Không được để trống");
            view.requestFocus();
            return false;
        } else return true;
    }

}
