package com.example.moneycare.ui.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.moneycare.data.model.Group;
import com.example.moneycare.ui.view.transaction.NewTransactionActivity;
import com.example.moneycare.R;
import com.example.moneycare.databinding.ActivityMainBinding;
import com.example.moneycare.ui.view.transaction.TransactionFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {
    private NavController navController;
    private ActivityMainBinding binding;
    ActivityResultLauncher<Intent> reloadTransListLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // There are no request codes
                    Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    Fragment firstFragment = navHostFragment.getChildFragmentManager().getFragments().get(0);
                    System.out.println("result");
                    if(firstFragment.getClass().equals(TransactionFragment.class)){
                        TransactionFragment fragment = (TransactionFragment) firstFragment;
                        fragment.showTransList();
                    }
                }
            }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initBottomNavbar();
        initNewTransBtn();
    }
    private void initBottomNavbar(){
        BottomNavigationView bottomNavigationView = binding.bottomNavigationView;
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
    private void initNewTransBtn(){
        binding.newTransBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewTransactionActivity.class);
                reloadTransListLauncher.launch(intent);
            }
        });
    }
    public ActivityResultLauncher<Intent> getReloadTransListLauncher(){
        return reloadTransListLauncher;
    }
}