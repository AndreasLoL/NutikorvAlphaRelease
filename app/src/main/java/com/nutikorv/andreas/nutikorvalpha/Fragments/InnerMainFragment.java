package com.nutikorv.andreas.nutikorvalpha.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
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
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.Model.ParentWrapper;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.nutikorv.andreas.nutikorvalpha.Adapters.ExpandableAdapter;
import com.nutikorv.andreas.nutikorvalpha.Adapters.ExpandableRecyclerViewAdapter;
import com.nutikorv.andreas.nutikorvalpha.Adapters.ProductListAdapter;
import com.nutikorv.andreas.nutikorvalpha.Adapters.SubCategoryExpandableRecyclerAdapter;
import com.nutikorv.andreas.nutikorvalpha.MainActivity;
import com.nutikorv.andreas.nutikorvalpha.Objects.AsyncResult;
import com.nutikorv.andreas.nutikorvalpha.Objects.Basket;
import com.nutikorv.andreas.nutikorvalpha.Objects.BasketStorage;
import com.nutikorv.andreas.nutikorvalpha.Objects.MainCategory;
import com.nutikorv.andreas.nutikorvalpha.Objects.Product;
import com.nutikorv.andreas.nutikorvalpha.Objects.ProductsFromURL;
import com.nutikorv.andreas.nutikorvalpha.Objects.ReadProducts;
import com.nutikorv.andreas.nutikorvalpha.Objects.Shop;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ANDREAS on 12.07.2016.
 */
public class InnerMainFragment extends Fragment implements ExpandableRecyclerViewAdapter.OnCardClickListner {

    private boolean lvBusy = false;
    private RecyclerView recycler;
    private ProductListAdapter adapterProducts;
    private CategoryExpandableAdapter adapter1;
    private TextView basket_preview;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private BasketStorage basketStorage;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_products_inner, container, false);

        sharedPref = this.getActivity().getSharedPreferences(
                GlobalParameters.BASKETS_PREFERENCE_SELECTED, Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        gson = new GsonBuilder().enableComplexMapKeySerialization().create();

        basketStorage = gson.fromJson(sharedPref.getString(GlobalParameters.BASKETS_PREFERENCE,
                null), BasketStorage.class);

        if (basketStorage == null) {
            basketStorage = new BasketStorage();
        }

        recycler = (RecyclerView) rootView.findViewById(R.id.main_recycler);

        basket_preview = (TextView) rootView.findViewById(R.id.basket_preview);

        MainCategory m;

        updateBasketPreview();

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


            adapter1 = new CategoryExpandableAdapter(getContext(), l1, this);
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
        }

        return rootView;
    }

    private void updatePreferences() {
        editor.putString(GlobalParameters.BASKETS_PREFERENCE, gson.toJson(basketStorage));
        editor.commit();
    }

    public void updateBasketPreview() {
        Log.i("------------>", "update");
        Basket selectedBasket = basketStorage.findSelectedBasket();
        if (selectedBasket != null) {
            basket_preview.setText(selectedBasket.getBasketName()
                    + " (" + selectedBasket.getProductsCount()
                    + " toodet) " + selectedBasket.getAllProductsPriceRange());
        } else {
            basket_preview.setText("Puudub aktiivne ostukorv!");
        }

    }


    @Override
    public void onResume() {
        super.onResume();

        MainCategory m;

        updateBasketPreview();


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


            adapter1 = new CategoryExpandableAdapter(getContext(), l1, this);


            recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            recycler.setAdapter(adapter1);

//            recycler.smoothScrollToPosition(0);

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


    @Override
    public void OnCardClicked(View view, int position) {
        Log.d("OnClick", "Card Position" + position);
    }

    private class CategoryExpandableAdapter extends
            ExpandableRecyclerAdapter<CategoryExpandableAdapter.MyParentViewHolder,
                    CategoryExpandableAdapter.MyChildViewHolder> {

        private LayoutInflater mInflater;
        private static final float INITIAL_POSITION = 0.0f;
        private static final float ROTATED_POSITION = 180f;
        private InnerMainFragment fragment;

        public CategoryExpandableAdapter(Context context, List<ParentListItem> itemList,
                                                    InnerMainFragment fragment) {
            super(itemList);
            mInflater = LayoutInflater.from(context);
            this.fragment = fragment;
        }

        @Override
        public MyParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
            View view = mInflater.inflate(R.layout.item_header, viewGroup, false);
            return new MyParentViewHolder(view);
        }


        @Override
        public MyChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
            View view = mInflater.inflate(R.layout.item_product_new, viewGroup, false);
            return new MyChildViewHolder(view);
        }

        @Override
        public void onBindParentViewHolder(MyParentViewHolder parentViewHolder, int position, ParentListItem parentListItem) {
            SubcategoryParentListItem subcategoryParentListItem = (SubcategoryParentListItem) parentListItem;
            parentViewHolder.lblListHeader.setText(subcategoryParentListItem.mTitle);

        }

        private void onSaleColor(boolean onSale, TextView t1) {
            if (onSale) {
                t1.setTextColor(Color.YELLOW);
            }
        }

        @SuppressWarnings("ResourceType")
        private void setPriceColors(Product currentProduct, MyChildViewHolder childViewHolder) {
            childViewHolder.selverPrice.setBackgroundResource(R.color.colorPrimaryDark);
            childViewHolder.maximaPrice.setBackgroundResource(R.color.colorPrimaryDark);
            childViewHolder.prismaPrice.setBackgroundResource(R.color.colorPrimaryDark);


            List<TextView> textViews = new ArrayList<>(Arrays.asList(childViewHolder.selverPrice, childViewHolder.prismaPrice, childViewHolder.maximaPrice));
            List<Shop> shops = currentProduct.getShops();
            Collections.sort(shops, new Comparator<Shop>() {
                @Override
                public int compare(Shop lhs, Shop rhs) {
                    if (Double.compare(lhs.getPrice(), rhs.getPrice()) < 0) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });
            for (int i = 0; i < textViews.size(); i++) {
                textViews.get(i).setText(shops.get(i).toString()); // SHOPS LENGTH MUST EQUAL TEXTVIEWS LENGTH
                textViews.get(i).setVisibility(shops.get(i).getVisibilityValue());
            }

        }

        @Override
        public void onBindChildViewHolder(final MyChildViewHolder childViewHolder, final int position, final Object childListItem) {

            setPriceColors((Product) childListItem, childViewHolder);

            Product subcategoryChildListItem = (Product) childListItem;
            childViewHolder.productName.setText(subcategoryChildListItem.getName());

            if (subcategoryChildListItem.getImgURL().equals("0")) {
                UrlImageViewHelper.setUrlDrawable(childViewHolder.img, "http://www.jordans.com/~/media/jordans%20redesign/no-image-found.ashx?h=275&la=en&w=275&hash=F87BC23F17E37D57E2A0B1CC6E2E3EEE312AAD5B");
            } else {
                UrlImageViewHelper.setUrlDrawable(childViewHolder.img, subcategoryChildListItem.getImgURL());
            }

            childViewHolder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int qt;
                    try {
                        qt = Integer.parseInt(childViewHolder.quantity.getText().toString());
                    } catch (Exception e) {
                        qt = 1;
                    }
                    basketStorage.findSelectedBasket().addToBasket((Product) childListItem, qt);
                    System.out.println("Added");
                    GlobalParameters.b.addToBasket((Product) childListItem, qt);
                    updatePreferences();
                    fragment.updateBasketPreview();
                    InputMethodManager imm = (InputMethodManager) v.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            });
        }

        public class MyParentViewHolder extends ParentViewHolder {

            public TextView lblListHeader;
            public ImageView mArrowExpandImageView;

            public MyParentViewHolder(View itemView) {
                super(itemView);
                lblListHeader = (TextView) itemView.findViewById(R.id.item_header_name);
                mArrowExpandImageView = (ImageView) itemView.findViewById(R.id.item_arrow);
            }

            @Override
            public void setExpanded(boolean expanded) {
                super.setExpanded(expanded);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    if (expanded) {
                        mArrowExpandImageView.setRotation(ROTATED_POSITION);
                    } else {
                        mArrowExpandImageView.setRotation(INITIAL_POSITION);
                    }
                }
            }

            @Override
            public void onExpansionToggled(boolean expanded) {
                super.onExpansionToggled(expanded);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    RotateAnimation rotateAnimation;
                    if (expanded) { // rotate clockwise
                        rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                                INITIAL_POSITION,
                                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                    } else { // rotate counterclockwise
                        rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION,
                                INITIAL_POSITION,
                                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                    }

                    rotateAnimation.setDuration(200);
                    rotateAnimation.setFillAfter(true);
                    mArrowExpandImageView.startAnimation(rotateAnimation);
                }
            }
        }

        @Override
        public void onParentListItemExpanded(int position) {
            Object parent = mItemList.get(position);
            collapseAllParents();    // Alternatively keep track of the single item that is expanded and explicitly collapse that row (more efficient)
            expandParent(((ParentWrapper) parent).getParentListItem());
        }


        @Override
        public void collapseAllParents() {
            super.collapseAllParents();
        }

        public class MyChildViewHolder extends ChildViewHolder {
            public TextView productName;
            public TextView selverPrice;
            public TextView maximaPrice;
            public TextView prismaPrice;
            public ImageView img;
            public Button add;
            public EditText quantity;

            public MyChildViewHolder(View itemView) {
                super(itemView);
                productName = (TextView) itemView.findViewById(R.id.item_name);
                selverPrice = (TextView) itemView.findViewById(R.id.selverPrice);
                maximaPrice = (TextView) itemView.findViewById(R.id.maximaPrice);
                prismaPrice = (TextView) itemView.findViewById(R.id.prismaPrice);
                img = (ImageView) itemView.findViewById(R.id.productImage);
                add = (Button) itemView.findViewById(R.id.addButton);
                quantity = (EditText) itemView.findViewById(R.id.quantityField);
            }
        }
    }



}
