package com.team.capestone.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.team.capestone.R;
import com.google.android.material.tabs.TabLayout;

public class HomeFragment extends Fragment {


    private ViewPager viewPager;
    private TabLayout tabLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = root.findViewById(R.id.viewPager);
        tabLayout = root.findViewById(R.id.tablayout);
        MyPager pager = new MyPager(getChildFragmentManager());
        viewPager.setAdapter(pager);
        tabLayout.setupWithViewPager(viewPager);

        return root;
    }

    static class MyPager extends FragmentStatePagerAdapter{

        MyPager(@NonNull FragmentManager manager) {
            super(manager, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if (position==0){
             return LearningFragment.newInstance();
            }else{
                return ChooseTestFragment.newInstance();
            }

        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return position == 0 ? "Learning" : "Testing";
        }
    }
}
