package com.example.moneycare.ui.view.transaction.wallet;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.moneycare.R;
import com.example.moneycare.data.model.Wallet;
import com.example.moneycare.databinding.ActivityUpdateWalletBinding;
import com.example.moneycare.ui.view.transaction.group.UpdateGroupActivity;
import com.example.moneycare.ui.viewmodel.transaction.UpdateWalletViewModel;
import com.example.moneycare.utils.ToastUtil;
import com.example.moneycare.utils.ValidationUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class UpdateWalletActivity extends AppCompatActivity {
    private UpdateWalletViewModel viewModel;
    private ActivityUpdateWalletBinding binding;
    private Wallet currentWallet;
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
        viewModel =  new ViewModelProvider(this).get(UpdateWalletViewModel.class);
        binding = ActivityUpdateWalletBinding.inflate(getLayoutInflater());
        binding.setUpdateWalletVM(viewModel);
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());

        initToolbar();
        initUpdateWalletBtn();
        initImagePicker();
        initWalletData();
    }
    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.update_app_bar);
        toolbar.setTitle("Sửa ví");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                UpdateWalletActivity.this.finish();
                onBackPressed();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.update_app_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.update_item:
                viewModel.switchUpdateMode();
                return true;
            case R.id.delete_item:
                showDeleteDialog();
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
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
    private void selectImage() {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        launcher.launch(intent);
    }
    private void handleSelectImage(Uri selectedImage){
        try {
            Bitmap bitmapImg = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
            viewModel.setImage(selectedImage, bitmapImg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initWalletData(){
        currentWallet = (Wallet) getIntent().getParcelableExtra("wallet");
        viewModel.initWallet(currentWallet);
    }
    private void initUpdateWalletBtn(){
        binding.updateWalletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check = checkAllFields();
                if(check){
                    viewModel.updateWallet(data->{
                        UpdateWalletActivity.this.setResult(Activity.RESULT_OK);
                        UpdateWalletActivity.this.finish();
                        ToastUtil.showToast(UpdateWalletActivity.this, "Cập nhật ví thành công");
                    },
                    data->{
                        ToastUtil.showToast(UpdateWalletActivity.this, "Lỗi! Cập nhật ví thất bại");
                    });
                }
            }
        });
    }

    private void showDeleteDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Bạn có muốn xóa ví?")
                .setMessage("Bạn cũng sẽ xóa tất cả giao dịch thuộc ví này và hành động này không thể hoàn tác.");

        setDialogEvent(dialog);
        dialog.show();
    }
    private void setDialogEvent(AlertDialog.Builder dialog){
        dialog.setPositiveButton("XÓA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteWallet();
            }
        });
        dialog.setNegativeButton("HỦY", null);
    }
    private void deleteWallet(){
        viewModel.deleteWallet(data->{
            if(isWalletCurrentSelected()) clearWalletFromPref();
            UpdateWalletActivity.this.finish();
            ToastUtil.showToast(UpdateWalletActivity.this, "Xóa ví thành công");
        },
        data->{
            ToastUtil.showToast(UpdateWalletActivity.this, "Lỗi! Xóa ví thất bại");
        });
    }
    private boolean isWalletCurrentSelected(){
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.transaction_preference), Context.MODE_PRIVATE);
        String savedWalletId = sharedPref.getString(getString(R.string.pref_key_current_wallet), "");
        return currentWallet.id.equals(savedWalletId);
    }
    private void clearWalletFromPref(){
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.transaction_preference), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.pref_key_current_wallet), "");
        editor.apply();
    }
    private boolean checkAllFields(){
        return ValidationUtil.checkEmpty(binding.updateWalletName) &&
                ValidationUtil.checkEmpty(binding.updateWalletMoney);
    }
}