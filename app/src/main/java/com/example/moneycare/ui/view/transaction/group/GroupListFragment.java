package com.example.moneycare.ui.view.transaction.group;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moneycare.data.model.Group;
import com.example.moneycare.databinding.FragmentGroupListBinding;

import java.util.List;

public class GroupListFragment extends Fragment {
    private final List<Group> groups;
    private FragmentGroupListBinding binding;
    private final AppCompatActivity activity;

    public GroupListFragment(AppCompatActivity activity, List<Group> groups) {
        this.activity = activity;
        this.groups = groups;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGroupListBinding.inflate(getLayoutInflater());
        initGroupList();
        return binding.getRoot();
    }
    public void initGroupList(){
        if(activity.getClass() == SelectGroupActivity.class){
            binding.groupList.setAdapter(new GroupSelectRvAdapter(activity, groups));
        }else if(activity.getClass() == ManageGroupActivity.class){
            binding.groupList.setAdapter(new GroupManageRvAdapter(activity, groups));
        }
    }
}