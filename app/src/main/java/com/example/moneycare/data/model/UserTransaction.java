package com.example.moneycare.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.moneycare.utils.FirestoreUtil;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserTransaction implements Parcelable {
    public long money;
    public Date date;
    public String group;
    public String note;
    public String wallet;
    public String eventId;
    public String id;
    public UserTransaction(String id, long money, String group, String note, Date date, String wallet, String eventId){
        this.id = id;
        this.money = money;
        this.date = date;
        this.group = group;
        this.note = note;
        this.wallet = wallet;
        this.eventId = eventId;
    }

    protected UserTransaction(Parcel in) {
        id = in.readString();
        money = in.readLong();
        group = in.readString();
        note = in.readString();
        wallet = in.readString();
        eventId = in.readString();
        date = (java.util.Date) in.readSerializable();
    }

    public static final Creator<UserTransaction> CREATOR = new Creator<UserTransaction>() {
        @Override
        public UserTransaction createFromParcel(Parcel in) {
            return new UserTransaction(in);
        }

        @Override
        public UserTransaction[] newArray(int size) {
            return new UserTransaction[size];
        }
    };

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("money", money);
        result.put("date", new Timestamp(date));
        result.put("group", FirestoreUtil.getReferenceFromPath(group));
        result.put("note", note);
        result.put("wallet", FirestoreUtil.getReferenceFromPath(wallet));
        result.put("eventId", eventId);
        return result;
    }
    public static UserTransaction fromMap(String id, Map<String, Object> map){
        Timestamp ts = (Timestamp) map.get("date");
        return new UserTransaction(
            id,
            (Long) map.get("money"),
            ((DocumentReference) map.get("group")).getPath(),
            ((String) map.get("note")),
            ts.toDate(),
            ((DocumentReference) map.get("wallet")).getPath(),
            ((String) map.get("eventId"))
        );
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeLong(money);
        parcel.writeString(group);
        parcel.writeString(note);
        parcel.writeString(wallet);
        parcel.writeString(eventId);
        parcel.writeSerializable(date);
    }
}
