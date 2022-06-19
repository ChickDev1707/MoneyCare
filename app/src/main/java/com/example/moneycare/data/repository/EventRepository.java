package com.example.moneycare.data.repository;

import androidx.annotation.NonNull;

import com.example.moneycare.data.model.Event;
import com.example.moneycare.data.model.Budget;
import com.example.moneycare.data.model.Event;
import com.example.moneycare.data.model.Group;
import com.example.moneycare.data.model.UserTransaction;
import com.example.moneycare.utils.DateTimeUtil;
import com.example.moneycare.utils.appinterface.FirestoreListCallback;
import com.example.moneycare.utils.appinterface.FirestoreObjectCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.List;

public class EventRepository {
    FirebaseFirestore db;
    public String idUser;

    public EventRepository(){
        db = FirebaseFirestore.getInstance();
        idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void fetchEvents(FirestoreListCallback callback){
        Query query =  db.collection("users").document(idUser).collection("events");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull  Task<QuerySnapshot> task) {
                List<Event> events = new ArrayList<Event>();
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot snapshot:task.getResult()){
                        Event event = Event.fromMap(snapshot.getId(), snapshot.getData());
                        events.add(event);
                    }
                    callback.onCallback(events);
                }
            }
        });
    }

    public void fetchEventById(FirestoreObjectCallback callback, String id){
        DocumentReference docRef = db.collection("users").document(idUser)
                .collection("events").document(id);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Event event = Event.fromMap(documentSnapshot.getId(), documentSnapshot.getData());
                callback.onCallback(event);
            }
        });
    }

    public void insertEvent(Event event){
        db.collection("users").document(idUser).collection("events")
                .add(event.toMap())
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        System.out.println("Insert successfully!!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error writing document:" + e);
                    }
                });
    }

    public void updateEvent(Event event){
        db.collection("users").document(idUser).collection("events").document(event.id)
            .update(event.toMap())
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    System.out.println("DocumentSnapshot successfully updated!");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("Error updating document" +  e);
                }
            });
    }
    public void changeStatus(String id, String status){
        db.collection("users").document(idUser).collection("events").document(id)
                .update("status", status)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error updating document" +  e);
                    }
                });
    }

    public void deleteEvent(String idEvent){
        DocumentReference docRef = db.collection("users").document(idUser).collection("events").document(idEvent);
        docRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("delete budgets success");
                    }
                });
    }
}
