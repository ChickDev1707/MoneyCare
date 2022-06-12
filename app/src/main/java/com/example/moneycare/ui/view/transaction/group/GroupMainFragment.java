package com.example.moneycare.ui.view.transaction.group;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moneycare.R;
import com.example.moneycare.data.model.Group;
import com.example.moneycare.data.repository.TransactionRepository;
import com.example.moneycare.databinding.FragmentGroupMainBinding;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class GroupMainFragment extends Fragment {
    private TransactionRepository repository = new TransactionRepository();
    private FragmentGroupMainBinding binding;
    public GroupMainFragment() {
        // Required empty public constructor

    }

    public static GroupMainFragment newInstance(String param1, String param2) {
        GroupMainFragment fragment = new GroupMainFragment();
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
        binding = FragmentGroupMainBinding.inflate(getLayoutInflater());

        initToolbar();
        initTab();
        return binding.getRoot();
    }
    private void initTab(){
        this.repository.fetchGroups(this::handleGroup);

    }
    private void handleGroup(List<Group> groups){
        GroupViewPagerAdapter adapter = new GroupViewPagerAdapter(this);
        List<Group> groups1 = new ArrayList<>();
        List<Group> groups2 = new ArrayList<>();
        for(Group group: groups){
            if(group.type){
                groups1.add(group);
            }else groups2.add(group);
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        GroupListFragment frag1 = new GroupListFragment(activity, groups1);
        GroupListFragment frag2 = new GroupListFragment(activity, groups2);
        adapter.addFragment("Khoản thu", frag1);
        adapter.addFragment("Khoản chi", frag2);
        binding.groupViewPager.setAdapter(adapter);

        new TabLayoutMediator(binding.groupTabLayout, binding.groupViewPager,
                (tab, position)-> tab.setText(adapter.getTitle(position))
        ).attach();
    }


    private class GroupViewPagerAdapter extends FragmentStateAdapter {
        private List<GroupListFragment> fragmentList = new ArrayList<>();
        private List<String> titleList = new ArrayList<>();
        public GroupViewPagerAdapter(Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            return fragmentList.size();
        }
        public void addFragment(String title, GroupListFragment fragment) {
            titleList.add(title);
            fragmentList.add(fragment);
        }
        public String getTitle(int position) {
            return titleList.get(position);
        }
    }
    private void initToolbar(){
        Toolbar toolbar = binding.basicAppBar;
        toolbar.setTitle(R.string.title_trans_group);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                getActivity().finish();
                getActivity().onBackPressed();
            }
        });
    }
}