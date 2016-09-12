package com.nutikorv.andreas.nutikorvalpha.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nutikorv.andreas.nutikorvalpha.Adapters.GridViewAdapter;
import com.nutikorv.andreas.nutikorvalpha.Objects.Basket;
import com.nutikorv.andreas.nutikorvalpha.Objects.Product;
import com.nutikorv.andreas.nutikorvalpha.Parameters.GlobalParameters;
import com.nutikorv.andreas.nutikorvalpha.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by AndreasPC on 9/12/2016.
 */
public class InstancedProductDisplayFragment extends Fragment {

    Activity mActivity;

    public static InstancedProductDisplayFragment newInstance(String products) {
        InstancedProductDisplayFragment myFragment = new InstancedProductDisplayFragment();

        Bundle args = new Bundle();
        args.putString("products", products + "");
        myFragment.setArguments(args);

        return myFragment;
    }

    public InstancedProductDisplayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_product_display, container, false);

        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

        String productsJSON = getArguments().getString("products", "")
                .replace("\\\"", "\"");

        productsJSON = productsJSON
                .substring(1, productsJSON.length() - 1);

        Basket prods = gson.fromJson(productsJSON, Basket.class);

        List<Product> prods1 = prods.getAllHashKeys();

        RecyclerView r1 =  (RecyclerView) rootView.findViewById(R.id.recView);

        r1.setLayoutManager(new LinearLayoutManager(getContext()));

        r1.setAdapter(new MyRecyclerAdapter(getContext(), prods1));


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


    private class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.CustomViewHolder> {
        private List<Product> products;
        private Context mContext;

        public MyRecyclerAdapter(Context context, List<Product> products) {
            this.products = products;
            this.mContext = context;
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_product, null);

            CustomViewHolder viewHolder = new CustomViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(CustomViewHolder customViewHolder, int position) {
             customViewHolder.textView.setText(products.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return products.size();
        }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView textView;

        public CustomViewHolder(View view) {
            super(view);
            this.textView = (TextView) view.findViewById(R.id.item_name);
        }
    }
    }

}