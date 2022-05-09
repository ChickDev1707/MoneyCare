package com.example.moneycare.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class LoadImage extends AsyncTask<String, Void, Bitmap> {
    private ImageView imgView;
    public LoadImage(ImageView view){
        this.imgView = view;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap decodedByte = null;
        try{
            byte[] decodedString = Base64.decode(strings[0], Base64.DEFAULT);
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }catch (Exception e){
            System.out.println("Can't convert to bitmap!");
        }
        return decodedByte;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        imgView.setImageBitmap(bitmap);
    }
}



