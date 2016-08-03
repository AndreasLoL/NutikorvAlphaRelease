package com.nutikorv.andreas.nutikorvalpha.Fragments;

/**
 * Created by ANDREAS on 12.07.2016.
 */
import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
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

    public static HomeFragment newInstance(String mainCategory) {
        HomeFragment myFragment = new HomeFragment();

        Bundle args = new Bundle();
        args.putString("products", mainCategory);
        myFragment.setArguments(args);

        return myFragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public String getVal() {
        return s;
    }

    String s;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        s = getArguments().getString("products", null);

        System.out.println("CALLED INSTANCED PRODUCTS: " + s);



        View rootView = inflater.inflate(R.layout.fragment_home, container, false);




        viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        pagerAdapter = new ViewPagerAdapter(this.getChildFragmentManager(), s);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(1);

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Tooted"));
        tabLayout.addTab(tabLayout.newTab().setText("Ostukorv"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

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

                if (position == 1 && fragment != null) {
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


}