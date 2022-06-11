package com.example.moneycare.ui.view.account;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.example.moneycare.R;
import com.example.moneycare.data.model.User;
import com.example.moneycare.databinding.ActivityInitWalletBinding;
import com.example.moneycare.databinding.FragmentTransactionListBinding;
import com.example.moneycare.ui.view.MainActivity;
import com.example.moneycare.ui.viewmodel.account.InitWalletViewModel;
import com.example.moneycare.ui.viewmodel.transaction.TransactionViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

public class InitWalletActivity extends AppCompatActivity {

    private ActivityInitWalletBinding binding;
    private InitWalletViewModel viewModel;
    private FirebaseAuth mFirebaseAuth;
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
        mFirebaseAuth = FirebaseAuth.getInstance();
        binding = ActivityInitWalletBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(InitWalletViewModel.class);
        binding.setInitWalletVM(viewModel);
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());

        initCreateWalletBtn();
        initImagePicker();
    }
    private void handleSelectImage(Uri selectedImage){
        try {
            Bitmap bitmapImg = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
            binding.walletImageView.setImageBitmap(bitmapImg);
            viewModel.setImage(bitmapImg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initImagePicker(){
        binding.selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                launcher.launch(intent);
            }
        });
    }

    private void initCreateWalletBtn(){
        binding.createWalletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = getIntent();
                User user;
                if(intent.hasExtra("email")){
                    // sign in by email
                    user = createUserFromIntent(intent);
                }else{
                    // sign in by google
                    user = createUserFromFirebaseUser();
                }
                viewModel.createUser(user, walletId-> {
                    Intent toMainIntent = new Intent(InitWalletActivity.this, MainActivity.class);
                    startActivity(toMainIntent);
                    finish();
                });
            }
        });
    }
    private User createUserFromIntent(Intent intent){
        String email = intent.getStringExtra("email");
        String username = intent.getStringExtra("username");
        String defaultImgUrl = "https://firebasestorage.googleapis.com/v0/b/money-care-df1b9.appspot.com/o/default-avatar.png?alt=media&token=153d36c9-8511-448b-bc68-9cc75879fdda";
        return new User(username, email, defaultImgUrl);
    }
    private User createUserFromFirebaseUser(){
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        return new User(user.getDisplayName(), user.getEmail(), user.getPhotoUrl().toString());
    }
}