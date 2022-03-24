package com.example.moneycare.ui.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.moneycare.R;
import com.example.moneycare.databinding.ActivityMainBinding;
import com.example.moneycare.ui.viewmodels.TestingViewModel;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TestingViewModel viewModel = new ViewModelProvider(this).get(TestingViewModel.class);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());

    }
}