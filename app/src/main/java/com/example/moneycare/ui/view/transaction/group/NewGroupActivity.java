package com.example.moneycare.ui.view.transaction.group;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.RadioGroup;

import com.example.moneycare.R;
import com.example.moneycare.databinding.ActivityNewGroupBinding;
import com.example.moneycare.ui.view.transaction.trans.NewTransactionActivity;
import com.example.moneycare.ui.viewmodel.transaction.NewGroupViewModel;
import com.example.moneycare.utils.ToastUtil;
import com.example.moneycare.utils.ValidationUtil;

import java.io.IOException;

public class NewGroupActivity extends AppCompatActivity {

    ActivityNewGroupBinding binding;
    NewGroupViewModel viewModel;
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

    private void handleSelectImage(Uri selectedImage){
        try {
            Bitmap bitmapImg = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
            binding.groupImageView.setImageBitmap(bitmapImg);
            viewModel.setImage(selectedImage, bitmapImg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewGroupBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(NewGroupViewModel.class);
        binding.setNewGroupVM(viewModel);
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());

        initToolbar();
        initImagePicker();
        initGroupTypeRadio();
        initSaveBtn();
    }
    private void initImagePicker(){
        binding.groupImgPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                selectImage();
            }
        });
    }
    private void initGroupTypeRadio(){
        binding.groupTypeRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                viewModel.setGroupType(getRadioValue(i));
            }
        });
    }
    private boolean getRadioValue(int radioId){
        if(radioId == R.id.radio_earning) return true;
        else return false;
    }
    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.basic_app_bar);
        toolbar.setTitle(R.string.title_new_group);
        this.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                onBackPressed();
            }
        });
    }
    private void selectImage()
    {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        launcher.launch(intent);
    }
    private void initSaveBtn(){
        binding.saveNewGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check = checkAllFields();
                if(check){
                    viewModel.saveNewGroup(data->{
                        NewGroupActivity.this.setResult(RESULT_OK);
                        NewGroupActivity.this.finish();
                        ToastUtil.showToast(NewGroupActivity.this, "Th??m nh??m th??nh c??ng");
                    },
                    data->{
                        ToastUtil.showToast(NewGroupActivity.this, "L???i! Th??m nh??m th???t b???i");
                    });

                }
            }
        });
    }
    private boolean checkAllFields(){
        return ValidationUtil.checkEmpty(binding.newGroupName) &&
                ValidationUtil.checkEmpty(binding.groupImgPicker);
    }

}