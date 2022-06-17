package com.example.moneycare.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Wallet implements Parcelable {
    public String id;
    public String name;
    public long money;
    public String image;
    public Wallet(){}
    public Wallet(String id, String name, long money, String image){
        this.id = id;
        this.name = name;
        this.money = money;
        this.image = image;
    }

    protected Wallet(Parcel in) {
        id = in.readString();
        name = in.readString();
        money = in.readLong();
        image = in.readString();
    }

    public static final Creator<Wallet> CREATOR = new Creator<Wallet>() {
        @Override
        public Wallet createFromParcel(Parcel in) {
            return new Wallet(in);
        }

        @Override
        public Wallet[] newArray(int size) {
            return new Wallet[size];
        }
    };
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("money", money);
        result.put("image", image);
        return result;
    }
    public static Wallet fromMap(String id, Map<String, Object> map){
        return new Wallet(
                id,
                map.get("name").toString(),
                (Long) map.get("money"),
                map.get("image").toString()
        );
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeLong(money);
        parcel.writeString(image);
    }
}
