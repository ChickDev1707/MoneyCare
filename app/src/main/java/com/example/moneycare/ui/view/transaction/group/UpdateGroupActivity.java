package com.example.moneycare.ui.view.transaction.group;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import com.example.moneycare.R;
import com.example.moneycare.data.model.Group;
import com.example.moneycare.data.model.UserTransaction;
import com.example.moneycare.databinding.ActivityUpdateGroupBinding;
import com.example.moneycare.databinding.ActivityUpdateTransactionBinding;
import com.example.moneycare.ui.view.transaction.trans.UpdateTransactionActivity;
import com.example.moneycare.ui.viewmodel.transaction.UpdateGroupViewModel;
import com.example.moneycare.ui.viewmodel.transaction.UpdateTransactionViewModel;
import com.example.moneycare.utils.ValidationUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class UpdateGroupActivity extends AppCompatActivity {
    private UpdateGroupViewModel viewModel;
    private ActivityUpdateGroupBinding binding;
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
        viewModel =  new ViewModelProvider(this).get(UpdateGroupViewModel.class);
        binding = ActivityUpdateGroupBinding.inflate(getLayoutInflater());
        binding.setUpdateGroupVM(viewModel);
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());

        initToolbar();
        initUpdateGroupBtn();
        initImagePicker();
        initGroupData();
    }
    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.update_app_bar);
        toolbar.setTitle("Sửa nhóm");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                UpdateGroupActivity.this.finish();
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
                viewModel.deleteGroup();
                UpdateGroupActivity.this.setResult(RESULT_OK);
                UpdateGroupActivity.this.finish();
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
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
    private void selectImage()
    {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        launcher.launch(intent);
    }
    private void handleSelectImage(Uri selectedImage){
        try {
            Bitmap bitmapImg = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
            binding.groupImageView.setImageBitmap(bitmapImg);
            viewModel.setImage(selectedImage, bitmapImg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private int getRadioId(boolean radioValue){
        if(radioValue) return R.id.radio_earning;
        else return R.id.radio_spending;
    }
    private void initGroupData(){
        Group group = (Group) getIntent().getParcelableExtra("group");
        viewModel.initGroup(group);

        RadioButton radBtn = findViewById(getRadioId(group.type));
        radBtn.setChecked(true);
    }
    private void initUpdateGroupBtn(){
        binding.updateGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check = checkAllFields();
                if(check){
                    viewModel.updateGroup();
                    UpdateGroupActivity.this.setResult(RESULT_OK);
                    UpdateGroupActivity.this.finish();
                }
            }
        });
    }
    private boolean checkAllFields(){
        return ValidationUtil.checkEmpty(binding.newGroupName) &&
                ValidationUtil.checkEmpty(binding.groupImgPicker);
    }
}