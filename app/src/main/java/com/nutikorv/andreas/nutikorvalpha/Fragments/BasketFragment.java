package com.nutikorv.andreas.nutikorvalpha.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nutikorv.andreas.nutikorvalpha.Adapters.BasketRecyclerAdapter;
import com.nutikorv.andreas.nutikorvalpha.Adapters.ShopRecyclerAdapter;
import com.nutikorv.andreas.nutikorvalpha.R;

/**
 * Created by ANDREAS on 12.07.2016.
 */
public class BasketFragment extends Fragment {

    public BasketFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_basket, container, false);

        RecyclerView r1 = (RecyclerView) rootView.findViewById(R.id.basketRecyclerView);


        r1.setLayoutManager(new LinearLayoutManager(getActivity()));

        r1.setAdapter(new BasketRecyclerAdapter(getContext()));

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
