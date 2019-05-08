package com.nhansen.bookproject.activity;

import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;

import com.nhansen.bookproject.R;
import com.nhansen.bookproject.activity.viewpager.TabFragmentPagerAdapter;

public class MainActivity extends UserActivityBase {

    TabFragmentPagerAdapter tabFragmentPagerAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.main_viewpager);
        tabFragmentPagerAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabFragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(2);

        // useless code?
        // app seems to function just fine without it
        // but the guide I used said to include it??
//        TabLayout tabLayout = findViewById(R.id.main_tablayout);
//        tabLayout.setupWithViewPager(viewPager);
    }

}
