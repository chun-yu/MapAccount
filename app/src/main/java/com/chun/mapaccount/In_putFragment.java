package com.chun.mapaccount;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class In_putFragment extends Fragment {
    private static final String TAG = "Tab1Fragment";

    private ViewPager mViewPager;
    private TabLayout tabLayout;
    MyDB myDB;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.in_putfragment, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.coast_container);
        tabLayout = (TabLayout) view.findViewById(R.id.day_tabs);
        initdata(mViewPager);
        tabLayout.setupWithViewPager(mViewPager);
        return view;
    }

    private void initdata(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getActivity().getSupportFragmentManager());
        myDB = new MyDB(getActivity());
        Year_coast_view year = new Year_coast_view();
        year.setMyDB(myDB);
        Month_coast_view month = new Month_coast_view();
        month.setMyDB(myDB);
        Day_coast_view day = new Day_coast_view();
        day.setMyDB(myDB);
        adapter.addFragment(year, getString(R.string.year));
        adapter.addFragment(month, getString(R.string.month));
        adapter.addFragment(day, getString(R.string.day));
        //adapter.getItemPosition(1);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(2);
        viewPager.setOffscreenPageLimit(0);
    }
}