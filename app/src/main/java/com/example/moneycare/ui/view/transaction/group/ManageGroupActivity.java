package com.example.moneycare.ui.view.transaction.group;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.moneycare.R;
import com.example.moneycare.databinding.ActivityManageGroupBinding;
import com.example.moneycare.databinding.FragmentGroupMainBinding;
import com.example.moneycare.ui.view.transaction.trans.TransactionFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ManageGroupActivity extends AppCompatActivity {
    private ActivityManageGroupBinding binding;
    ActivityResultLauncher<Intent> reloadGroupListLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    reloadGroupList();
                }
            }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityManageGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initOpenAddGroupBtn();
    }
    private void initOpenAddGroupBtn(){
        FloatingActionButton btn = findViewById(R.id.floating_add_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageGroupActivity.this, NewGroupActivity.class);
                reloadGroupListLauncher.launch(intent);
            }
        });
    }
    public ActivityResultLauncher<Intent> getReloadGroupListLauncher(){
        return reloadGroupListLauncher;
    }
    public void reloadGroupList(){
        Fragment firstFragment = binding.fragmentMainGroup.getFragment();
        if(firstFragment instanceof GroupMainFragment){
            ((GroupMainFragment) firstFragment).initTab();
        }
    }
}