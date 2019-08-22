package com.zoobie.android.myapplication.adapters;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.zoobie.android.myapplication.ui.tabs.FirstTabFragment;
import com.zoobie.android.myapplication.ui.tabs.SecondTabFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment item;
        position++;
        Bundle bundle = new Bundle();
        if (position == 1) item = new FirstTabFragment();
        else item = new SecondTabFragment();

        bundle.putString("message", "Fragment :" + position);
        bundle.putInt("id", position);
        item.setArguments(bundle);
        return item;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        position++;

        if (position == 1) {
            return "Stats";
        } else {
            return "Receipts";
        }
    }
}
