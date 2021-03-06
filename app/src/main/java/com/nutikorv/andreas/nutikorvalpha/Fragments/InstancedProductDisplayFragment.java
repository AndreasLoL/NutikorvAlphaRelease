package com.nutikorv.andreas.nutikorvalpha.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.nutikorv.andreas.nutikorvalpha.Adapters.GridViewAdapter;
import com.nutikorv.andreas.nutikorvalpha.Objects.Basket;
import com.nutikorv.andreas.nutikorvalpha.Objects.BasketStorage;
import com.nutikorv.andreas.nutikorvalpha.Objects.Product;
import com.nutikorv.andreas.nutikorvalpha.Objects.Shop;
import com.nutikorv.andreas.nutikorvalpha.Parameters.GlobalParameters;
import com.nutikorv.andreas.nutikorvalpha.R;
import com.nutikorv.andreas.nutikorvalpha.SharedPreferenceEditor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by AndreasPC on 9/12/2016.
 */
public class InstancedProductDisplayFragment extends Fragment {

    private TextView prismaPrice;
    private TextView maximaPrice;
    private TextView selverPrice;
    private Basket prods;
    private RecyclerView r1;
    private SharedPreferences sharedPref;
    private Gson gson;
    private BasketStorage basketStorage;
    private int dataCase;

    Activity mActivity;

    public static InstancedProductDisplayFragment newInstance(int dataCase, String products) {
        InstancedProductDisplayFragment myFragment = new InstancedProductDisplayFragment();

        Bundle args = new Bundle();
        args.putString("products", products + "");
        args.putInt("dataCase", dataCase);
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

        gson = new GsonBuilder().enableComplexMapKeySerialization().create();

        r1 =  (RecyclerView) rootView.findViewById(R.id.recView);
        r1.setLayoutManager(new LinearLayoutManager(getContext()));
        TextView basketNameDisplay = (TextView) rootView.findViewById(R.id.basketViewName);

        sharedPref = this.getActivity().getSharedPreferences(
                GlobalParameters.BASKETS_PREFERENCE_SELECTED, Context.MODE_PRIVATE);

        dataCase = getArguments().getInt("dataCase");

        switch (getArguments().getInt("dataCase")){
            case 0:
                System.out.println("CASE SHAREDPREF");
                basketStorage = gson.fromJson(sharedPref.getString(GlobalParameters.BASKETS_PREFERENCE,
                        null), BasketStorage.class);

                if (basketStorage == null) {
                    basketStorage = new BasketStorage();
                }

                if (basketStorage.findSelectedBasket() != null) {
                    r1.setAdapter(new MyRecyclerAdapter(getContext(), basketStorage.findSelectedBasket().getAllProducts()));
                    basketNameDisplay.setText(basketStorage.findSelectedBasket().getBasketName());
                } else {
                    r1.setAdapter(new MyRecyclerAdapter(getContext(), new LinkedHashMap<Product, Integer>()));
                }
                break;
            case 1:
                System.out.println("CASE ARGUMENTS");
                String productsJSON = getArguments().getString("products", "")
                        .replace("\\\"", "\"");

                productsJSON = productsJSON
                        .substring(1, productsJSON.length() - 1);

                System.out.println(productsJSON);

                try {
                    prods = gson.fromJson(productsJSON, Basket.class);
                } catch (JsonSyntaxException jsonE) {
                    prods = null;
                }

                if (prods != null) {
                    r1.setAdapter(new MyRecyclerAdapter(getContext(), prods.getAllProducts()));
                    basketNameDisplay.setText(prods.getBasketName());
                } else {
                    r1.setAdapter(new MyRecyclerAdapter(getContext(), new LinkedHashMap<Product, Integer>()));
                    basketNameDisplay.setText("Valitud ostukorv puudub!");
                }

        }

        selverPrice = (TextView) rootView.findViewById(R.id.selverPrice);
        maximaPrice = (TextView) rootView.findViewById(R.id.maximaPrice);
        prismaPrice = (TextView) rootView.findViewById(R.id.prismaPrice);

        updatePrices();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        System.out.println("CASE " + dataCase);

        Log.i("----------->", "ON RESUME CALLED");

        switch (dataCase){
            case 0:
                gson = new GsonBuilder().enableComplexMapKeySerialization().create();

                basketStorage = gson.fromJson(sharedPref.getString(GlobalParameters.BASKETS_PREFERENCE,
                        null), BasketStorage.class);

                if (basketStorage == null) {
                    basketStorage = new BasketStorage();
                }

                if (basketStorage.findSelectedBasket() != null) {
                    r1.setAdapter(new MyRecyclerAdapter(getContext(), basketStorage.findSelectedBasket().getAllProducts()));
                } else {
                    r1.setAdapter(new MyRecyclerAdapter(getContext(), new LinkedHashMap<Product, Integer>()));
                }
                break;
            case 1:
                System.out.println("CASE ARGUMENTS");
                String productsJSON = getArguments().getString("products", "")
                        .replace("\\\"", "\"");

                productsJSON = productsJSON
                        .substring(1, productsJSON.length() - 1);

                System.out.println(productsJSON);

                try {
                    prods = gson.fromJson(productsJSON, Basket.class);
                } catch (JsonSyntaxException jsonE) {
                    prods = null;
                }

                if (prods != null) {
                    r1.setAdapter(new MyRecyclerAdapter(getContext(), prods.getAllProducts()));
                } else {
                    r1.setAdapter(new MyRecyclerAdapter(getContext(), new LinkedHashMap<Product, Integer>()));
                }
        }

        updatePrices();


    }

    private void updatePreferences() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(GlobalParameters.BASKETS_PREFERENCE, gson.toJson(basketStorage));
        editor.commit();
    }

    public void updatePrices() {
        Basket selectedBasket = null;
        switch (dataCase) {
            case 0:
                if (basketStorage != null && basketStorage.findSelectedBasket() != null
                        && basketStorage.findSelectedBasket().getAllProducts() != null) {
                    selectedBasket = basketStorage.findSelectedBasket();

                }
                break;
            case 1:
                if (prods != null && prods.getAllProducts() != null) {
                    selectedBasket = prods;
                }
                break;
        }

        if (selectedBasket != null) {
            selverPrice.setText(String.format("%.2f", selectedBasket.getSelverPrice()) + "€");
            prismaPrice.setText(String.format("%.2f", selectedBasket.getPrismaPrice()) + "€");
            maximaPrice.setText(String.format("%.2f", selectedBasket.getMaximaPrice()) + "€");
        } else {
            selverPrice.setText(0 + "€");
            maximaPrice.setText(0 + "€");
            prismaPrice.setText(0 + "€");
        }

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
        private HashMap<Product, Integer> hashProducts;

        public MyRecyclerAdapter(Context context, LinkedHashMap<Product, Integer> hashProducts) {
            this.mContext = context;
            this.hashProducts = hashProducts;
            this.products = new ArrayList<>(hashProducts.keySet());
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.basket_cardview_item, null);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CustomViewHolder customViewHolder, final int position) {
            final Product p = products.get(position);

            setPriceColors(p, customViewHolder);

            customViewHolder.title.setText(p.getName());
            customViewHolder.quantity.setText("Kogus: " + hashProducts.get(p));
            customViewHolder.price.setText(p.getPriceRange(hashProducts.get(p)));

            if (p.getImgURL().equals("0")) {
                UrlImageViewHelper.setUrlDrawable(customViewHolder.thumbnail, "http://www.jordans.com/~/media/jordans%20redesign/no-image-found.ashx?h=275&la=en&w=275&hash=F87BC23F17E37D57E2A0B1CC6E2E3EEE312AAD5B");
            } else {
                UrlImageViewHelper.setUrlDrawable(customViewHolder.thumbnail, p.getImgURL());
            }

            customViewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (dataCase){
                        case 0:
                            basketStorage.findSelectedBasket().removeProduct(p);
                            SharedPreferenceEditor.removeProductFromSelectedBasket(p, getContext());
                            break;
                        case 1:
                            prods.removeProduct(p);
                            break;
                    }
                    products.remove(p);
                    notifyDataSetChanged();
                    updatePreferences();
                    updatePrices();
                }
            });
        }

        @SuppressWarnings("ResourceType")
        private void setPriceColors(Product currentProduct, CustomViewHolder childViewHolder) {
            childViewHolder.selverPiecePrice.setBackgroundResource(R.color.colorPrimaryDark);
            childViewHolder.maximaPiecePrice.setBackgroundResource(R.color.colorPrimaryDark);
            childViewHolder.prismaPiecePrice.setBackgroundResource(R.color.colorPrimaryDark);


            List<TextView> textViews = new ArrayList<>(Arrays.asList(
                    childViewHolder.selverPiecePrice, childViewHolder.prismaPiecePrice, childViewHolder.maximaPiecePrice));
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

//                if (shops.get(i).isOnSale()) {
//                    textViews.get(i).setBackgroundResource(R.color.colorPrimaryLight);
//                }
            }

        }

        @Override
        public int getItemCount() {
            return products.size();
        }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumbnail;
        private TextView title;
        private TextView price;
        private ImageButton button;
        private TextView quantity;
        private TextView selverPiecePrice;
        private TextView prismaPiecePrice;
        private TextView maximaPiecePrice;

        public CustomViewHolder(View view) {
            super(view);
            this.thumbnail = (ImageView) view.findViewById(R.id.productImage);
            this.thumbnail.setAnimation(null);
            this.title = (TextView) view.findViewById(R.id.productName);
            this.button = (ImageButton) view.findViewById(R.id.deleteProduct);
            this.price = (TextView) view.findViewById(R.id.totalPrice);
            this.quantity = (TextView) view.findViewById(R.id.quantity);
            this.maximaPiecePrice = (TextView) view.findViewById(R.id.maximaPrice);
            this.selverPiecePrice = (TextView) view.findViewById(R.id.selverPrice);
            this.prismaPiecePrice = (TextView) view.findViewById(R.id.prismaPrice);
        }
    }
    }

}