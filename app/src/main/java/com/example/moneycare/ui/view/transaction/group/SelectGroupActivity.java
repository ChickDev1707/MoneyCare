package com.example.moneycare.ui.view.transaction.group;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.moneycare.R;
import com.example.moneycare.data.model.Group;
import com.example.moneycare.data.repository.TransactionRepository;
import com.example.moneycare.databinding.ActivitySelectGroupBinding;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class SelectGroupActivity extends AppCompatActivity {
    private TransactionRepository repository = new TransactionRepository();
    private ActivitySelectGroupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySelectGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//
//        initToolbar();
//        initTab();
    }
//    private void initTab(){
//        this.repository.fetchGroups(groups -> {
//            handleGroup((List<Group>) groups);
//        });
//    }
//    private void handleGroup(List<Group> groups){
//        GroupViewPagerAdapter adapter = new GroupViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
//        List<Group> groups1 = new ArrayList<>();
//        List<Group> groups2 = new ArrayList<>();
//        for(Group group: groups){
//            if(group.type){
//                groups1.add(group);
//            }else groups2.add(group);
//        }
//
//        GroupListFragment frag1 = new GroupListFragment(this, groups1);
//        GroupListFragment frag2 = new GroupListFragment(this, groups2);
//        adapter.addFragment("Khoản thu", frag1);
//        adapter.addFragment("Khoản chi", frag2);
//        binding.groupViewPager.setAdapter(adapter);
//
//        new TabLayoutMediator(binding.groupTabLayout, binding.groupViewPager,
//                (tab, position)-> tab.setText(adapter.getTitle(position))
//        ).attach();
//    }
//
//    private class GroupViewPagerAdapter extends FragmentStateAdapter{
//        private List<GroupListFragment> fragmentList = new ArrayList<>();
//        private List<String> titleList = new ArrayList<>();
//        public GroupViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
//            super(fragmentManager, lifecycle);
//        }
//
//        @NonNull
//        @Override
//        public Fragment createFragment(int position) {
//            return fragmentList.get(position);
//        }
//
//        @Override
//        public int getItemCount() {
//            return fragmentList.size();
//        }
//        public void addFragment(String title, GroupListFragment fragment) {
//            titleList.add(title);
//            fragmentList.add(fragment);
//        }
//        public String getTitle(int position) {
//            return titleList.get(position);
//        }
//    }
//    private void initToolbar(){
//        Toolbar toolbar = findViewById(R.id.basic_app_bar);
//        toolbar.setTitle(R.string.title_trans_group);
//        this.setSupportActionBar(toolbar);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // back button pressed
//                GroupActivity.this.finish();
//                onBackPressed();
//            }
//        });
//    }
}