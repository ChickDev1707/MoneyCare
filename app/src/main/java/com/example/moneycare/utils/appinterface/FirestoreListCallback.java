package com.example.moneycare.utils.appinterface;

import java.util.List;

public interface FirestoreListCallback<T> {
    public void onCallback(List<T> data);
}
