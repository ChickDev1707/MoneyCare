package com.example.moneycare.ui.view.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.moneycare.R;
import com.example.moneycare.data.repository.LoginRepository;
import com.example.moneycare.databinding.FragmentAccountBinding;
import com.example.moneycare.ui.view.IntroActivity;
import com.example.moneycare.ui.view.transaction.group.ManageGroupActivity;
import com.example.moneycare.utils.ImageUtil;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountFragment extends Fragment {
    FragmentAccountBinding binding;
    LoginRepository repository;
    public AccountFragment() {
        // Required empty public constructor
    }

    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(getLayoutInflater());
        repository = new LoginRepository();

        initAccountItemList();
        initUserInfo();
        return binding.getRoot();
    }
    private void initAccountItemList(){
        binding.accountItemList.setAdapter(new AccountItemRecyclerViewAdapter((Activity) getActivity()));
    }
    private void initUserInfo(){
        repository.fetchCurrentUserData(user->{
            binding.userName.setText(user.name);
            binding.userEmail.setText(user.email);
            Glide.with(this).load(user.photoUrl).into(binding.userImage);
        });
    }
}