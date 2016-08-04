package com.nutikorv.andreas.nutikorvalpha.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.innodroid.expandablerecycler.ExpandableRecyclerAdapter;
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


    Activity mActivity;
    ExpandableRecyclerViewAdapter adapter;
    private boolean lvBusy = false;
    RecyclerView recycler;
    ProductListAdapter adapterProducts;

    private GridLayoutManager lLayout;

    public static InnerMainFragment newInstance(String mainCategory) {
        InnerMainFragment myFragment = new InnerMainFragment();

        Bundle args = new Bundle();
        args.putString("products", mainCategory);
        myFragment.setArguments(args);

        return myFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_products_inner, container, false);

//        ExpandableListView choices = (ExpandableListView) rootView.findViewById(R.id.listView);
        recycler = (RecyclerView) rootView.findViewById(R.id.main_recycler);

        String s = getArguments().getString("products", null);

        System.out.println("CALLED INSTANCED PRODUCTS FROM INNER FRAGMENT: " + s);

        Gson g = new Gson();

        MainCategory m = g.fromJson(s, MainCategory.class);



        if (GlobalParameters.r != null) {
//            final HashMap<String, List<Product>> t = new HashMap<>();
//            List<SubcategoryParentListItem> subcategoryParentListItems = new ArrayList<>();
//            for (int i = 0; i < 5; i++) {
//                SubcategoryParentListItem eachParentItem = new SubcategoryParentListItem();
//                subcategoryParentListItems.add(eachParentItem);
//            }
//
//            List<ParentListItem> parentListItems = new ArrayList<>();
//            for (SubcategoryParentListItem subcategoryParentListItem : subcategoryParentListItems) {
//                List<SubcategoryChildListItem> childItemList = new ArrayList<>();
//                for (int i = 0; i < 5; i++) {
//                    childItemList.add(new SubcategoryChildListItem());
//                }
//                subcategoryParentListItem.setChildItemList(childItemList);
//                parentListItems.add(subcategoryParentListItem);
//            }

            List<ParentListItem> l1 = new ArrayList<>();


            for (SubCategory subs : m.getSubCategories()) {
                SubcategoryParentListItem m1 = new SubcategoryParentListItem(subs);
                m1.setChildItemList(subs.getProducts());
                l1.add(m1);
            }



            recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            recycler.setAdapter(new SubCategoryExpandableRecyclerAdapter(getContext(), l1));

            System.out.println("GENERATED RECYCLER");


//
//            lLayout = new GridLayoutManager(getActivity(), 2);
//
//            adapter = new ExpandableRecyclerViewAdapter(getActivity(), m);
//            adapter.setMode(ExpandableRecyclerAdapter.MODE_ACCORDION);
//            recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
//            recycler.setAdapter(adapter);

            return rootView;
        } else {
//            GlobalParameters.r = new ReadProducts(mActivity, "products.txt");
//
//            final HashMap<String, List<Product>> t = new HashMap<>();
//
//
//            new ProductsFromURL(new AsyncResult() {
//                @Override
//                public void onResult(JSONObject object) {
//                    try {
//                        JSONArray rows = object.getJSONArray("entry");
//                        for (int i = 0; i < rows.length(); i++) {
//                            JSONObject t1 = rows.getJSONObject(i);
//                            JSONObject t2 = t1.getJSONObject("content");
//                            String t3 = t2.getString("$t");
//                            GlobalParameters.r.loadProducstFromArary(t3.split(", "));
//                        }
//
//                        t.put("viinad", GlobalParameters.r.getProductsList());
//
//                        t.put("Liha", GlobalParameters.r.getProductsList());
//
//                        lLayout = new GridLayoutManager(getActivity(), 2);
//
//                        adapter = new ExpandableRecyclerViewAdapter(getActivity(), t);
//                        adapter.setMode(ExpandableRecyclerAdapter.MODE_ACCORDION);
//                        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
//                        recycler.setAdapter(adapter);
//
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).execute("https://spreadsheets.google.com/feeds/list/1SSpGG-PnuUMxZbJ5qeuzhoIifj65nuKCmjZq48zkAO0/od6/public/basic?alt=json");

        }



//        t.put("viinad", r.getProductsList());
//
//        t.put("Liha", r.getProductsList());
//
//        lLayout = new GridLayoutManager(getActivity(), 2);
//
//        adapter = new ExpandableRecyclerViewAdapter(getActivity(), t);
//        adapter.setMode(ExpandableRecyclerAdapter.MODE_ACCORDION);
//        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recycler.setAdapter(adapter);
//        adapter.setOnCardClickListner(this);






//        adapter = new ExpandableAdapter(mActivity, r.getGroupList(), r.getFoodCollection());
//
//
//
//
//        choices.setAdapter(adapter);
//
//        if (GlobalParameters.b == null) {
//            GlobalParameters.b = new Basket("My temporary basket!");
//        }
//
//        choices.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//
//                createProductDialog((Product) adapter.getChild(groupPosition, childPosition), getContext());
//
//
////                Product temp = (Product) adapter.getChild(groupPosition, childPosition);
////
////                GlobalParameters.b.addToBasket(temp);
////                Toast.makeText(getContext(), "Added to basket!", Toast.LENGTH_SHORT).show();
//
//                return false;
//            }
//        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
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
