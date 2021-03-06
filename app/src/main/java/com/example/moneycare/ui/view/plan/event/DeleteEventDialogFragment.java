package com.example.moneycare.ui.view.plan.event;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.moneycare.R;
import com.example.moneycare.data.model.Event;
import com.example.moneycare.data.repository.EventRepository;
import com.example.moneycare.utils.ToastUtil;
import com.example.moneycare.utils.appenum.DeleteType;

public class DeleteEventDialogFragment extends DialogFragment {

    AppCompatActivity currentActivity;
    View dialogView;
    Button deleteEventBtn;
    Button deleteAllBtn;
    Event selectedEvent;
    EventRepository repository;
    public DeleteEventDialogFragment(AppCompatActivity activity, Event selectedEvent) {
        // Required empty public constructor
        this.currentActivity = activity;
        this.selectedEvent = selectedEvent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
        repository = new EventRepository();
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.fragment_delete_event_dialog, null);
        builder.setView(dialogView)
                .setTitle("B???n c?? mu???n x??a s??? ki???n?")
                .setMessage("B???n c?? th??? ch??? x??a s??? ki???n n??y ho???c x??a s??? ki???n v?? t???t c??? giao d???ch c???a s??? ki???n n??y.");

        initElements();
        setDeleteAllBtnEvent();
        setDeleteEventBtnEvent();
        return builder.create();
    }
    private void initElements(){
        deleteAllBtn = dialogView.findViewById(R.id.delete_all_btn);
        deleteEventBtn = dialogView.findViewById(R.id.delete_event_btn);
    }
    private void setDeleteAllBtnEvent(){
        deleteAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repository.deleteEvent(selectedEvent.id, DeleteType.DEEP_DELETE, data -> {finishActivity();}, data -> {ToastUtil.showToast(currentActivity, "L???i! X??a s??? ki???n th???t b???i");});
            }
        });
    }
    private void setDeleteEventBtnEvent(){
        deleteEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repository.deleteEvent(selectedEvent.id, DeleteType.DELETE_CURRENT, data -> {finishActivity();}, data -> {ToastUtil.showToast(currentActivity, "L???i! X??a s??? ki???n th???t b???i");});
            }
        });
    }
    private void finishActivity(){
        currentActivity.finish();
        ToastUtil.showToast(currentActivity, "X??a s??? ki???n th??nh c??ng");
    }
    public void showDialog(){
        this.show(this.currentActivity.getSupportFragmentManager(), "dialog");
    }
}