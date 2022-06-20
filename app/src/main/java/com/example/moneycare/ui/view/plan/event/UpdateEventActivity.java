package com.example.moneycare.ui.view.plan.event;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.example.moneycare.R;
import com.example.moneycare.data.model.Event;
import com.example.moneycare.data.model.Wallet;
import com.example.moneycare.databinding.ActivityUpdateEventBinding;
import com.example.moneycare.ui.view.transaction.wallet.SelectWalletActivity;
import com.example.moneycare.ui.viewmodel.plan.EventViewModel;
import com.example.moneycare.utils.ImageUtil;
import com.example.moneycare.utils.ImageLoader;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.io.IOException;
import java.util.Date;

public class UpdateEventActivity extends AppCompatActivity {
    private ActivityUpdateEventBinding binding;
    private EventViewModel eventVM;
    private Event eventUpdated;
    private String walletName;
    private ActivityResultLauncher<Intent> toSelectWalletActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        eventVM.wallet.setValue(data.getParcelableExtra("wallet"));
                    }
                }
            });

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        Uri selectedImage = intent.getData();
                        handleSelectImage(selectedImage);
                    }
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUpdateEventBinding.inflate(getLayoutInflater());
        eventVM = new ViewModelProvider(this).get(EventViewModel.class);
        binding.setEventVM(eventVM);
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        eventUpdated = intent.getParcelableExtra("event");
        walletName = intent.getStringExtra("walletName");

        loadData();
        initToolbar();
        initPickDateInput();
        initSelectWallet();
        initImagePicker();
        handleUpdateEvent();
    }

    private void loadData(){
        eventVM.eventName.setValue(eventUpdated.name);
        eventVM.endDate.setValue(eventUpdated.endDate);
        Wallet wallet = new Wallet();
        wallet.name = walletName;
        eventVM.wallet.setValue(wallet);
        ImageLoader imageLoader = new ImageLoader(binding.selectEventUpdateImg);
        imageLoader.execute(eventUpdated.image);
        eventVM.image.setValue(ImageUtil.toBitmap(eventUpdated.image));
    }
    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.basic_app_bar);
        toolbar.setTitle("Sửa sự kiện");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateEventActivity.this.finish();
                UpdateEventActivity.this.onBackPressed();
            }
        });
    }
    private void initPickDateInput(){
        binding.updateEventEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
                MaterialDatePicker datePicker = builder
                        .setTitleText("Chọn ngày kết thúc")
                        .setSelection(eventUpdated.endDate.getTime())
                        .build();
                datePicker.show(getSupportFragmentManager(), "Pick date");
                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        Date pickedDate = new Date((Long) selection);
                        if(pickedDate.after(new Date())){
                            eventVM.endDate.setValue(pickedDate);
                        }
                        else {
                            System.out.println("Không được chọn ngày trong quá khứ");
                        }
                    }
                });
            }
        });
    }

    private void initSelectWallet(){
        binding.updateEventWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateEventActivity.this, SelectWalletActivity.class);
                toSelectWalletActivity.launch(intent);
            }
        });
    }
    private void initImagePicker(){
        binding.selectEventUpdateImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                launcher.launch(intent);
            }
        });
    }

    private void handleSelectImage(Uri selectedImage){
        try {
            Bitmap bitmapImg = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
            binding.selectEventUpdateImg.setImageBitmap(bitmapImg);
            eventVM.setImage(selectedImage, bitmapImg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleUpdateEvent(){
        binding.btnUpdateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eventVM.eventName.getValue().isEmpty() || eventVM.endDate.getValue() == null ){
                    return;
                }
                eventVM.updateEvent(eventUpdated.id);
                Intent intent = getIntent();
                intent.putExtra("eventUpdated", new Event(eventUpdated.id, eventVM.eventName.getValue(),  eventVM.endDate.getValue(),
                        eventVM.image.getValue() == null?"" : ImageUtil.toBase64(eventVM.image.getValue()), eventVM.wallet.getValue().id, "ongoing" ));
                UpdateEventActivity.this.setResult(Activity.RESULT_OK, intent);
                UpdateEventActivity.this.finish();
            }
        });
    }

}