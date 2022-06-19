package com.example.moneycare.utils.appinterface;

import java.util.List;

public interface FirestoreMultiListCallback<T> {
    public void onCallback(List<T> list1, List<T> list2);
}