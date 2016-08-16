package com.nutikorv.andreas.nutikorvalpha.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.nutikorv.andreas.nutikorvalpha.Adapters.ExpandableAdapter;
import com.nutikorv.andreas.nutikorvalpha.Adapters.ExpandableRecyclerViewAdapter;
import com.nutikorv.andreas.nutikorvalpha.Adapters.ProductListAdapter;
import com.nutikorv.andreas.nutikorvalpha.Adapters.SubCategoryExpandableRecyclerAdapter;
import com.nutikorv.andreas.nutikorvalpha.MainActivity;
import com.nutikorv.andreas.nutikorvalpha.Objects.AsyncResult;
import com.nutikorv.andreas.nutikorvalpha.Objects.Basket;
import com.nutikorv.andreas.nutikorvalpha.Objects.MainCategory;
import com.nutikorv.andreas.nutikorvalpha.Objects.Product;
import com.nutikorv.andreas.nutikorvalpha.Objects.ProductsFromURL;
import com.nutikorv.andreas.nutikorvalpha.Objects.ReadProducts;
import com.nutikorv.andreas.nutikorvalpha.Objects.SubCategory;
import com.nutikorv.andreas.nutikorvalpha.Objects.SubcategoryChildListItem;
import com.nutikorv.andreas.nutikorvalpha.Objects.SubcategoryParentListItem;
import com.nutikorv.andreas.nutikorvalpha.Parameters.GlobalParameters;
import com.nutikorv.andreas.nutikorvalpha.R;
import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ANDREAS on 12.07.2016.
 */
public class InnerMainFragment extends Fragment implements ExpandableRecyclerViewAdapter.OnCardClickListner {

    private boolean lvBusy = false;
    private RecyclerView recycler;
    private ProductListAdapter adapterProducts;
    private SubCategoryExpandableRecyclerAdapter adapter1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_products_inner, container, false);

        recycler = (RecyclerView) rootView.findViewById(R.id.main_recycler);

//        String s = getArguments().getString("products", null);
//
//        Gson g = new Gson();
//
//        MainCategory m = g.fromJson(s, MainCategory.class);
//
//        if (adapter1 != null) {
//            adapter1.onRestoreInstanceState(savedInstanceState);
//        }


        MainCategory m;

        if (GlobalParameters.selectedCategory == null) {
            m = GlobalParameters.r.getCategories().get(0);
        } else {
            m = GlobalParameters.selectedCategory;
        }


        if (GlobalParameters.r != null) {

            List<ParentListItem> l1 = new ArrayList<>();


            for (SubCategory subs : m.getSubCategories()) {
                SubcategoryParentListItem m1 = new SubcategoryParentListItem(subs);
                m1.setChildItemList(subs.getProducts());
                l1.add(m1);
            }


            adapter1 = new SubCategoryExpandableRecyclerAdapter(getContext(), l1);
            adapter1.setExpandCollapseListener(new ExpandableRecyclerAdapter.ExpandCollapseListener() {
                @Override
                public void onListItemExpanded(int position) {
                    Log.v("-------------->", "EXPAND DETECTED");
                }

                @Override
                public void onListItemCollapsed(int position) {
                    Log.v("-------------->", "COLLAPSE DETECTED");
                }
            });


            recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            recycler.setAdapter(adapter1);



            recycler.smoothScrollToPosition(0);
//
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    recycler.smoothScrollToPosition(2);
                }
            }, 1000);


            return rootView;
        }

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();

        MainCategory m;


        if (GlobalParameters.selectedCategory == null) {
            m = GlobalParameters.r.getCategories().get(0);
        } else {
            m = GlobalParameters.selectedCategory;
        }

        if (GlobalParameters.r != null) {

            List<ParentListItem> l1 = new ArrayList<>();


            for (SubCategory subs : m.getSubCategories()) {
                SubcategoryParentListItem m1 = new SubcategoryParentListItem(subs);
                m1.setChildItemList(subs.getProducts());
                l1.add(m1);
            }


            adapter1 = new SubCategoryExpandableRecyclerAdapter(getContext(), l1);


            recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            recycler.setAdapter(adapter1);

            recycler.smoothScrollToPosition(0);

//
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                public void run() {
//                    recycler.smoothScrollToPosition(0);
//                }
//            }, 10000);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    recycler.smoothScrollToPosition(2);
                }
            }, 1000);
        }
    }

    private void createProductDialog(Product p, Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_product);
        dialog.setTitle(p.getName());

        final Product p2 = p;

        TextView title = (TextView) dialog.findViewById(R.id.dialogTitle);
        title.setText(p.getName());


        TextView text = (TextView) dialog.findViewById(R.id.dialogDescription);
        text.setText(p.getProductDescription());
        ImageView image = (ImageView) dialog.findViewById(R.id.dialogImage);
        UrlImageViewHelper.setUrlDrawable(image, p.getImgURL());

        TextView selverPrice = (TextView) dialog.findViewById(R.id.selverPrice);
        selverPrice.setText("Selveri hind: " + String.format("%.2f", p.getSelverPrice()) + "€");

        TextView prismaPrice = (TextView) dialog.findViewById(R.id.prismaPrice);
        prismaPrice.setText("Prisma hind: " + String.format("%.2f", p.getPrismaPrice()) + "€");

        TextView maximaPrice = (TextView) dialog.findViewById(R.id.maximaPrice);
        maximaPrice.setText("Maxima hind: " + String.format("%.2f", p.getMaximaPrice()) + "€");


        Button dialogButton = (Button) dialog.findViewById(R.id.addProduct);



        dialogButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GlobalParameters.b.addToBasket(p2, 1);
                Toast.makeText(getContext(), "Added to basket!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                lvBusy = false;
                adapterProducts.notifyDataSetChanged();
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                lvBusy = true;
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                lvBusy = true;
                break;
        }
    }

    public boolean isLvBusy(){
        return lvBusy;
    }

    @Override
    public void OnCardClicked(View view, int position) {
        Log.d("OnClick", "Card Position" + position);
    }

}
