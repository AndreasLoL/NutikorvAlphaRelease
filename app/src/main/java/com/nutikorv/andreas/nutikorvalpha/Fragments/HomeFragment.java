package com.nutikorv.andreas.nutikorvalpha.Fragments;

/**
 * Created by ANDREAS on 12.07.2016.
 */
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.nutikorv.andreas.nutikorvalpha.Adapters.ProductListAdapter;
import com.nutikorv.andreas.nutikorvalpha.Adapters.ViewPagerAdapter;
import com.nutikorv.andreas.nutikorvalpha.Objects.MainCategory;
import com.nutikorv.andreas.nutikorvalpha.Objects.SubCategory;
import com.nutikorv.andreas.nutikorvalpha.R;


public class HomeFragment extends Fragment {

    public static ViewPager viewPager;
    ViewPagerAdapter pagerAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setRetainInstance(true);

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        pagerAdapter = new ViewPagerAdapter(this.getChildFragmentManager());

        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(1);

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Kategooriad"));
        tabLayout.addTab(tabLayout.newTab().setText("Tooted"));
        tabLayout.addTab(tabLayout.newTab().setText("Ostukorv"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabTextColors(getResources().getColor(R.color.textColorPrimary), getResources().getColor(R.color.selectedTab));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.selectedTab));

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Fragment fragment = ((ViewPagerAdapter) viewPager.getAdapter()).getFragment(position);

                if (position == 2 && fragment != null) {
                    fragment.onResume();
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void resetCategories() {
        Fragment fragment = ((ViewPagerAdapter) viewPager.getAdapter()).getFragment(1);
        fragment.onResume();
    }



}