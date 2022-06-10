package com.example.moneycare.ui.view.transaction.wallet;

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
import android.widget.Toast;

import com.example.moneycare.R;
import com.example.moneycare.databinding.ActivityNewWalletBinding;
import com.example.moneycare.ui.view.transaction.trans.NewTransactionActivity;
import com.example.moneycare.ui.viewmodel.transaction.NewWalletViewModel;
import com.example.moneycare.utils.ValidationUtil;

import java.io.IOException;

public class NewWalletActivity extends AppCompatActivity {

    ActivityNewWalletBinding binding;
    NewWalletViewModel viewModel;
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
            binding.walletImageView.setImageBitmap(bitmapImg);
            viewModel.setImage(selectedImage, bitmapImg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewWalletBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(NewWalletViewModel.class);
        binding.setNewWalletVM(viewModel);
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());

        initToolbar();
        initImagePicker();
        initSaveBtn();
    }
    private void initImagePicker(){
        binding.walletImgPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                selectImage();
            }
        });
    }

    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.basic_app_bar);
        toolbar.setTitle(R.string.title_new_wallet);
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
        binding.saveNewWalletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check = checkAllFields();
                if(check){
                    viewModel.saveNewWallet(data->{
                        NewWalletActivity.this.setResult(Activity.RESULT_OK);
                        NewWalletActivity.this.finish();
                        Toast toast =  Toast.makeText(NewWalletActivity.this, "Thêm ví thành công", Toast.LENGTH_SHORT);
                        toast.show();
                    },
                    data->{
                        Toast toast =  Toast.makeText(NewWalletActivity.this, "Lỗi! Thêm ví thất bại", Toast.LENGTH_SHORT);
                        toast.show();
                    });
                }
            }
        });
    }
    private boolean checkAllFields(){
        return ValidationUtil.checkEmpty(binding.newWalletName) &&
                ValidationUtil.checkEmpty(binding.newWalletMoney) &&
                ValidationUtil.checkEmpty(binding.walletImgPicker);
    }
}