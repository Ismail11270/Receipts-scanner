package com.zoobie.android.myapplication.adapters;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.zoobie.android.myapplication.activity.tabs.FirstTabFragment;
import com.zoobie.android.myapplication.activity.tabs.SecondTabFragment;
import com.zoobie.android.myapplication.activity.tabs.ThirdTabFragment;

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
        else if(position == 2) item = new SecondTabFragment();
        else item = new ThirdTabFragment();
        bundle.putString("message", "" + position);
        bundle.putInt("id", position);
        item.setArguments(bundle);
        System.out.println(position + " POS");
        return item;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        position++;

        if (position == 1) {
            return "Stats";
        } else if(position == 2){
            return "Receipts";
        }else return "Products";
    }


}
