package com.example.moneycare.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.moneycare.utils.FirestoreUtil;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Event implements Parcelable {
    public String id;
    public String name;
    public Date endDate;
    public String image;
    public String status; // ongoing |  end
    public Event(){}
    public Event(String id, String name, Date date, String image, String status){
        this.id = id;
        this.name = name;
        this.endDate = date;
        this.image = image;
        this.status = status;
    }

    protected Event(Parcel in) {
        id = in.readString();
        name = in.readString();
        endDate = (java.util.Date) in.readSerializable();
        image = in.readString();
        status = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Exclude
    public Map<String, Object> toMap() {
        String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("image", image);
        result.put("endDate", new Timestamp(endDate));
        result.put("status", status);
        return result;
    }

    public static Event fromMap(String id, Map<String, Object> map){
        Timestamp ts = (Timestamp) map.get("endDate");
        Object walletObj =  map.get("wallet");
        return new Event(
                id,
                map.get("name").toString(),
                ts.toDate(),
                map.get("image").toString(),
                map.get("status").toString()
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
        parcel.writeSerializable(endDate);
        parcel.writeString(image);
        parcel.writeString(status);
    }
}
