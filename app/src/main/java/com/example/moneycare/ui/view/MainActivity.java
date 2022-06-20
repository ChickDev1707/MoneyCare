package com.example.moneycare.ui.view;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.moneycare.databinding.ActivityMainBinding;
import com.example.moneycare.ui.view.transaction.trans.NewTransactionActivity;
import com.example.moneycare.R;
import com.example.moneycare.ui.view.transaction.trans.TransactionFragment;
import com.example.moneycare.utils.appinterface.ReloadTransactionActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends ReloadTransactionActivity {
    private NavController navController;
    private ActivityMainBinding binding;

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
                startActivity(intent);
            }
        });
    }
    @Override
    public void reloadTransactionList(){
        Fragment navHostFragment = this.getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        Fragment firstFragment = navHostFragment.getChildFragmentManager().getFragments().get(0);
        if(firstFragment.getClass().equals(TransactionFragment.class)){
            TransactionFragment fragment = (TransactionFragment) firstFragment;
            fragment.initElements();
        }
    }
}