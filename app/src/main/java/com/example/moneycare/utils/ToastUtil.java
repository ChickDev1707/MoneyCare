package com.example.moneycare.utils;

import android.content.Context;
import android.widget.Toast;

import com.example.moneycare.ui.view.transaction.trans.NewTransactionActivity;

public class ToastUtil {
    public void showToast(Context context, String message){
        Toast toast =  Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
