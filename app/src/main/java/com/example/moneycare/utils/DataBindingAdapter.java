package com.example.moneycare.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseMethod;

public class DataBindingAdapter {
    @BindingAdapter("imageBitmap")
    public static void loadImage(ImageView imageView, Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }
}
