package com.example.moneycare.ui.view.transaction.group;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.moneycare.R;
import com.example.moneycare.databinding.ActivityManageGroupBinding;

public class ManageGroupActivity extends AppCompatActivity {
    private ActivityManageGroupBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityManageGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initOpenAddGroupBtn();
    }
    private void initOpenAddGroupBtn(){
        binding.openAddGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageGroupActivity.this, NewGroupActivity.class);
                startActivity(intent);
            }
        });
    }
}