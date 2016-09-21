package com.nutikorv.andreas.nutikorvalpha.Fragments;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.nutikorv.andreas.nutikorvalpha.Adapters.GridViewScrollable;
import com.nutikorv.andreas.nutikorvalpha.Adapters.ShopRecyclerAdapter;
import com.nutikorv.andreas.nutikorvalpha.Adapters.SponsorRecyclerAdapter;
import com.nutikorv.andreas.nutikorvalpha.Adapters.CategoryGridViewAdapter;
import com.nutikorv.andreas.nutikorvalpha.Objects.OnSaleProduct;
import com.nutikorv.andreas.nutikorvalpha.Parameters.GlobalParameters;
import com.nutikorv.andreas.nutikorvalpha.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ANDREAS on 12.07.2016.
 */
public class SalesFragment extends Fragment {

    private TextView newsText;

    private TextView shopText;

    private TextView categoriesText;

    public SalesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sales, container, false);

        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Light.ttf");

        newsText = (TextView) rootView.findViewById(R.id.textNews);

        newsText.setTypeface(font);

        shopText = (TextView) rootView.findViewById(R.id.textShops);

        shopText.setTypeface(font);

        categoriesText = (TextView) rootView.findViewById(R.id.textCategories);

        categoriesText.setTypeface(font);

        GridViewScrollable g1 = (GridViewScrollable) rootView.findViewById(R.id.salesGridView);

        g1.setAdapter(new CategoryGridViewAdapter(getContext()));

        g1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                // Create new fragment and transaction
                Fragment newFragment = new SaleDisplayFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.container_body, newFragment);
                transaction.addToBackStack(null);

                transaction.commit();

            }
        });

//        g1.setAdapter(new SalesGridViewAdapter(getContext()));

        RecyclerView r1 = (RecyclerView) rootView.findViewById(R.id.sponsorGridView);

        List<OnSaleProduct> sponsor = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            sponsor.add(GlobalParameters.productsStorage.getOnSaleProducts().get(i + 10));
        }

        r1.setAdapter(new SponsorRecyclerAdapter(getContext(), sponsor));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);

        r1.setLayoutManager(gridLayoutManager);

        RecyclerView r2 = (RecyclerView) rootView.findViewById(R.id.shopGridView);

        r2.setAdapter(new ShopRecyclerAdapter(getContext()));

        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);

        r2.setLayoutManager(gridLayoutManager2);

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
