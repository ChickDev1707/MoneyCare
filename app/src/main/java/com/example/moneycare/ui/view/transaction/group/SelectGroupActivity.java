package com.example.moneycare.ui.view.transaction.group;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.moneycare.databinding.ActivitySelectGroupBinding;


public class SelectGroupActivity extends AppCompatActivity {
    private ActivitySelectGroupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySelectGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}