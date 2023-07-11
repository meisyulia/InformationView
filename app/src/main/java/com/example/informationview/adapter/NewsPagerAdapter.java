package com.example.informationview.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.informationview.uis.fragment.NewsTabFragment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

public class NewsPagerAdapter extends FragmentPagerAdapter {

    private final ArrayList<String> titleList;
    private final LinkedHashMap<String, String> topicMap;

    public NewsPagerAdapter(@NonNull FragmentManager fm,  LinkedHashMap<String, String> topicMap) {
        super(fm);
        this.topicMap = topicMap;
        titleList = new ArrayList<>(topicMap.keySet());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return NewsTabFragment.getInstance(topicMap.get(titleList.get(position)));
    }

    @Override
    public int getCount() {
        return titleList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
