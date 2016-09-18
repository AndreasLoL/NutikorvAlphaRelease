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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
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
import com.nutikorv.andreas.nutikorvalpha.Objects.Product;
import com.nutikorv.andreas.nutikorvalpha.Parameters.GlobalParameters;
import com.nutikorv.andreas.nutikorvalpha.R;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;

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

    private List<Basket> baskets;

    private List<Basket> basketsWithoutSelected;

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

        baskets = new ArrayList<>();
        Basket selectedBasket = gson.fromJson(sharedPref.getString(GlobalParameters.BASKETS_PREFERENCE_SELECTED, null), Basket.class);

        if (selectedBasket != null) {
            System.out.println("ADDED SETSELECTED");
            selectedBasket.setSelected(true);
            baskets.add(selectedBasket);
        }

        RecyclerView r1 = (RecyclerView) rootView.findViewById(R.id.basketRecyclerView);

        if (sharedPref.getString(GlobalParameters.BASKETS_PREFERENCE, null) == null) {
            baskets = new ArrayList<>();
            basketsWithoutSelected = new ArrayList<>();
            baskets.add(gson.fromJson(sharedPref.getString(GlobalParameters.BASKETS_PREFERENCE_SELECTED, null), Basket.class));
            updatePreferences();
        } else {
            basketStorage = gson.fromJson(sharedPref.getString(GlobalParameters.BASKETS_PREFERENCE,
                    null), BasketStorage.class);
            if (basketStorage != null) {
                basketsWithoutSelected = basketStorage.getBaskets();
                baskets.addAll(basketsWithoutSelected);
            } else {
                basketsWithoutSelected = new ArrayList<>();
            }

        }


        r1.setLayoutManager(new LinearLayoutManager(getActivity()));

        a1 = new SimpleAdapter(r1, baskets, this);

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
                                basketsWithoutSelected.add(tempBasket);
                                baskets.add(tempBasket);
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
        System.out.println("BASKETS JSON!! " + gson.toJson(new BasketStorage(basketsWithoutSelected)));
        editor.putString(GlobalParameters.BASKETS_PREFERENCE, gson.toJson(new BasketStorage(basketsWithoutSelected)));
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

    private static class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.ViewHolder> {
        private static final int UNSELECTED = -1;
        private ColorGenerator generator = ColorGenerator.MATERIAL;
        private RecyclerView recyclerView;
        private int selectedItem = UNSELECTED;
        private List<Basket> baskets;
        private BasketFragment f;

        public SimpleAdapter(RecyclerView recyclerView, List<Basket> baskets, BasketFragment f) {
            this.recyclerView = recyclerView;
            this.baskets = baskets;
            this.f = f;
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
            return baskets.size();
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
                this.position = position;

                final String basketID = baskets.get(position).getBasketName();

                basketName.setText(basketID);

                letter = "" + basketID.charAt(0);

                TextDrawable drawable = TextDrawable.builder()
                        .buildRound(letter, generator.getColor(basketName));

                imageLetter.setImageDrawable(drawable);

                itemView.setSelected(false);

                expandedLayout.collapse(false);

                if (baskets.get(position).isSelected()) {
                    System.out.println("IS SELECTED!");
                    toggleSwitch.setChecked(true);
                } else {
                    toggleSwitch.setChecked(false);
                }

//                toggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        if (isChecked) {
//                            for (Basket b: baskets) {
//                                b.setSelected(false);
//                            }
//                        }
//                        baskets.get(position).setSelected(true);
//                        notifyDataSetChanged();
//                    }
//                });

                toggleSwitch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!baskets.get(position).isSelected()) {
                            for (Basket b: baskets) {
                                b.setSelected(false);
                            }
                        }
                        baskets.get(position).setSelected(true);
                        notifyDataSetChanged();
                    }
                });



                productAmount.setText(baskets.get(position).getProductsCount() + " TOODET");
                productPriceRange.setText(baskets.get(position).getAllProductsPriceRange());

                callProductsFragment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

                        Fragment newFragment = new InstancedProductDisplayFragment().newInstance(gson.toJson(gson.toJson(baskets.get(position))));
                        FragmentTransaction transaction = f.getFragmentManager().beginTransaction();

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
