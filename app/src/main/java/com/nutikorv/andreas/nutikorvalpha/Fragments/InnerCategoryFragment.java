package com.nutikorv.andreas.nutikorvalpha.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.nutikorv.andreas.nutikorvalpha.Adapters.GridViewAdapter;
import com.nutikorv.andreas.nutikorvalpha.Parameters.GlobalParameters;
import com.nutikorv.andreas.nutikorvalpha.R;

/**
 * Created by ANDREAS on 13.08.2016.
 */
public class InnerCategoryFragment extends Fragment {

    ViewPager viewPager = HomeFragment.viewPager;

    Activity mActivity;

    GridView gridView;

    public InnerCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_categories_inner, container, false);

//        ListView l1 = (ListView) rootView.findViewById(R.id.categoriesView);
//
//        ArrayAdapter<String> a1 = new ArrayAdapter<String>(getContext(),  android.R.layout.simple_list_item_1, GlobalParameters.productsStorage.getCategoriesString());
//
//        l1.setAdapter(a1);
//
//        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                GlobalParameters.selectedCategory = GlobalParameters.productsStorage.getCategories().get(position);
//                viewPager.setCurrentItem(1, true);
//
//
//                HomeFragment TabOfFragManage1 = (HomeFragment) getParentFragment();
//
//                TabOfFragManage1.resetCategories();
//            }
//        });

        gridView = (GridView) rootView.findViewById(R.id.gridView);

        gridView.setAdapter(new GridViewAdapter(getContext(), GlobalParameters.productsStorage.getCategories()));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                GlobalParameters.selectedCategory = GlobalParameters.productsStorage.getCategories().get(position);
                viewPager.setCurrentItem(1, true);
                HomeFragment TabOfFragManage1 = (HomeFragment) getParentFragment();
                TabOfFragManage1.resetCategories();
            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
