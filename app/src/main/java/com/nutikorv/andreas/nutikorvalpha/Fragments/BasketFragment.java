package com.nutikorv.andreas.nutikorvalpha.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nutikorv.andreas.nutikorvalpha.Objects.Basket;
import com.nutikorv.andreas.nutikorvalpha.Objects.BasketStorage;
import com.nutikorv.andreas.nutikorvalpha.Parameters.GlobalParameters;
import com.nutikorv.andreas.nutikorvalpha.R;

import net.cachapa.expandablelayout.ExpandableLayout;

/**
 * Created by ANDREAS on 12.07.2016.
 */
public class BasketFragment extends Fragment {

    private Button addBasket;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    private SimpleAdapter a1;

    private BasketStorage basketStorage;

    private Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

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

        sharedPref = this.getActivity().getSharedPreferences(
                GlobalParameters.BASKETS_PREFERENCE_SELECTED, Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        System.out.println(sharedPref.getString(GlobalParameters.BASKETS_PREFERENCE, null));

        if (sharedPref.getString(GlobalParameters.BASKETS_PREFERENCE, null) == null ||
                sharedPref.getString(GlobalParameters.BASKETS_PREFERENCE, null).equals("null")) {
            basketStorage = new BasketStorage();
            updatePreferences();
            Log.i("-->", "Created new basket");
        } else {
            System.out.println(sharedPref.getString(GlobalParameters.BASKETS_PREFERENCE,
                    null));
            basketStorage = gson.fromJson(sharedPref.getString(GlobalParameters.BASKETS_PREFERENCE, null), BasketStorage.class);

            Log.i("->", "Loaded stored basket!");
        }


        RecyclerView r1 = (RecyclerView) rootView.findViewById(R.id.basketRecyclerView);
        r1.setLayoutManager(new LinearLayoutManager(getActivity()));
        a1 = new SimpleAdapter(r1, this);
        r1.setAdapter(a1);

        addBasket = (Button) rootView.findViewById(R.id.addBasket);

        addBasket.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_add_basket);

                final EditText et1 = (EditText) dialog.findViewById(R.id.basketNameField);


                Button dialogButton = (Button) dialog.findViewById(R.id.addToBasketList);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (et1.getText().toString().length() > 0) {
                            if (!et1.getText().toString().trim().isEmpty()) {
                                Basket tempBasket = new Basket(et1.getText().toString());
                                basketStorage.addBasket(tempBasket);
                                a1.notifyDataSetChanged();
                                updatePreferences();
                            }
                        }
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }

        });

        // Inflate the layout for this fragment
        return rootView;
    }

    private void updatePreferences() {
        editor.putString(GlobalParameters.BASKETS_PREFERENCE, gson.toJson(basketStorage));
        editor.commit();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.ViewHolder> {
        private static final int UNSELECTED = -1;
        private ColorGenerator generator = ColorGenerator.MATERIAL;
        private RecyclerView recyclerView;
        private int selectedItem = UNSELECTED;
        private BasketFragment thisFragment;

        public SimpleAdapter(RecyclerView recyclerView, BasketFragment thisFragment) {
            this.recyclerView = recyclerView;
            this.thisFragment = thisFragment;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.basket_menu_item_new, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            if (basketStorage == null) {
                Log.i("--->!","Failed to load basket storage!");
                return 0;
            }
            return basketStorage.getBaskets().size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private TextView expandButton;
            private int position;
            private String letter;
            protected ImageView imageLetter;
            protected TextView basketName;
            protected ExpandableLayout expandedLayout;
            protected TextView productAmount;
            protected TextView productPriceRange;
            protected Button callProductsFragment;
            protected Switch toggleSwitch;

            public ViewHolder(View itemView) {
                super(itemView);

                imageLetter = (ImageView) itemView.findViewById(R.id.item_letter);
                basketName = (TextView) itemView.findViewById(R.id.item_title);
                expandedLayout = (ExpandableLayout) itemView.findViewById(R.id.expandedLayout);
                productAmount = (TextView) itemView.findViewById(R.id.productAmountText);
                productPriceRange = (TextView) itemView.findViewById(R.id.priceRangeText);
                callProductsFragment = (Button) itemView.findViewById(R.id.callFragmentButton);
                toggleSwitch = (Switch) itemView.findViewById(R.id.toggleSwitch);

                itemView.setOnClickListener(this);

//                expandButton.setOnClickListener(this);
            }

            public void bind(final int position) {
                final Basket currentBasket = basketStorage.getBaskets().get(position);

                final String basketID = currentBasket.getBasketName();
                basketName.setText(basketID);

                letter = "" + basketID.charAt(0);

                TextDrawable drawable = TextDrawable.builder()
                        .buildRound(letter, generator.getColor(basketName));

                imageLetter.setImageDrawable(drawable);

                itemView.setSelected(false);

                expandedLayout.collapse(false);

                if (currentBasket.isSelected()) {
                    System.out.println("Selected basket: " + currentBasket.getBasketName());
                    toggleSwitch.setChecked(true);
                } else {
                    toggleSwitch.setChecked(false);
                }

                toggleSwitch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!currentBasket.isSelected()) {
                            Log.i("Selection change", "Selection change detected!");
//                            basketStorage.setSelectedBasket(currentBasket);
                            if (basketStorage.findSelectedBasket() != null) {
                                basketStorage.findSelectedBasket().setSelected(false);
                            }
                            currentBasket.setSelected(true);
                            updatePreferences();
                        }
                        notifyDataSetChanged();
                    }
                });

                productAmount.setText(currentBasket.getProductsCount() + " TOODET");
                productPriceRange.setText(currentBasket.getAllProductsPriceRange());

                callProductsFragment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

                        Fragment newFragment = new InstancedProductDisplayFragment().newInstance(1, gson.toJson(gson.toJson(currentBasket)));
                        FragmentTransaction transaction = thisFragment.getFragmentManager().beginTransaction();

                        transaction.replace(R.id.container_body, newFragment);
                        transaction.addToBackStack(null);

                        transaction.commit();
                    }
                });
            }

            @Override
            public void onClick(View view) {
                ViewHolder holder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(selectedItem);
                if (holder != null) {
                    holder.itemView.setSelected(false);
                    holder.expandedLayout.collapse();
                }

                if (position == selectedItem) {
                    selectedItem = UNSELECTED;
                } else {
                    itemView.setSelected(true);
                    expandedLayout.expand();
                    selectedItem = position;
                }
            }
        }
    }
}
