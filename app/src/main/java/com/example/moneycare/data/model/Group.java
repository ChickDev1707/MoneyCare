package com.example.moneycare.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Group implements Parcelable {
    public String id;
    public String name;
    public boolean type;
    public String imgUrl;
    public Group(){}
    public Group(String id, String name, boolean type, String imgUrl){
        this.id = id;
        this.name = name;
        this.type = type;
        this.imgUrl = imgUrl;
    }

    protected Group(Parcel in) {
        id = in.readString();
        name = in.readString();
        type = in.readByte() != 0;
        imgUrl = in.readString();
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("type", type);
        result.put("imgUrl", imgUrl);
        return result;
    }
    public static Group fromMap(String id, Map<String, Object> map){
        return new Group(
                id,
                map.get("name").toString(),
                (Boolean) map.get("type"),
                map.get("imgUrl").toString()
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
        parcel.writeByte((byte) (type ? 1 : 0));
        parcel.writeString(imgUrl);
    }
}
