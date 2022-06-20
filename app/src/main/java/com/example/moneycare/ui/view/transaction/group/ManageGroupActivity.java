package com.example.moneycare.ui.view.transaction.group;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.moneycare.R;
import com.example.moneycare.databinding.ActivityManageGroupBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
        FloatingActionButton btn = findViewById(R.id.floating_add_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageGroupActivity.this, NewGroupActivity.class);
                startActivity(intent);
            }
        });
    }
    public void loadGroupList(){
        Fragment firstFragment = binding.fragmentMainGroup.getFragment();
        if(firstFragment instanceof GroupMainFragment){
            ((GroupMainFragment) firstFragment).initTab();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadGroupList();
    }
}