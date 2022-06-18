package com.example.moneycare.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.ImageView;

public class ImageLoader extends AsyncTask<String, Void, Bitmap> {
    private final ImageView imgView;
    public ImageLoader(ImageView view){
        this.imgView = view;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap decodedByte = null;
        try{
            decodedByte= ImageUtil.toBitmap(strings[0]);
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



