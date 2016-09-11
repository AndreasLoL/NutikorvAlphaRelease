package com.nutikorv.andreas.nutikorvalpha.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.nutikorv.andreas.nutikorvalpha.Adapters.BasketListView;
import com.nutikorv.andreas.nutikorvalpha.Adapters.BasketRecyclerAdapter;
import com.nutikorv.andreas.nutikorvalpha.Adapters.ShopRecyclerAdapter;
import com.nutikorv.andreas.nutikorvalpha.Objects.Basket;
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

//    private BasketRecyclerAdapter a1;

    private SimpleAdapter a1;

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

        final List<Basket> baskets = new ArrayList<>();

        Basket b1 = GlobalParameters.b;

//
        baskets.add(b1);
        baskets.add(new Basket("Test item 1"));
        baskets.add(new Basket("Test item 2"));
        baskets.add(new Basket("Test item 3"));
        baskets.add(new Basket("Test item 4"));
        baskets.add(new Basket("Test item 5"));
        baskets.add(new Basket("Test item 6"));
        baskets.add(new Basket("Test item 7"));
        baskets.add(new Basket("Test item 8"));
//        baskets.add(new Basket("Test basket 7"));
//        baskets.add(new Basket("Test basket 8"));
//        baskets.add(new Basket("Test basket 9"));



        r1.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//        a1 = new BasketRecyclerAdapter(getContext(), baskets, r1);
//
//        r1.setAdapter(a1);

//        a1 = new BasketListView(getContext(), baskets, r1);
//
//        r1.setAdapter(a1);

        a1 = new SimpleAdapter(r1, baskets);

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
                            baskets.add(new Basket(et1.getText().toString()));
                            a1.notifyDataSetChanged();

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

        public SimpleAdapter(RecyclerView recyclerView, List<Basket> baskets) {
            this.recyclerView = recyclerView;
            this.baskets = baskets;
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

            public ViewHolder(View itemView) {
                super(itemView);

                imageLetter = (ImageView) itemView.findViewById(R.id.item_letter);
                basketName = (TextView) itemView.findViewById(R.id.item_title);
                expandedLayout = (ExpandableLayout) itemView.findViewById(R.id.expandedLayout);
                productAmount = (TextView) itemView.findViewById(R.id.productAmountText);
                productPriceRange = (TextView) itemView.findViewById(R.id.priceRangeText);

                itemView.setOnClickListener(this);

//                expandButton.setOnClickListener(this);
            }

            public void bind(int position) {
                this.position = position;

                String basketID = baskets.get(position).getBasketName();

                basketName.setText(basketID);

                letter = "" + basketID.charAt(0);

                TextDrawable drawable = TextDrawable.builder()
                        .buildRound(letter, generator.getColor(basketName));

                imageLetter.setImageDrawable(drawable);

                itemView.setSelected(false);
                expandedLayout.collapse(false);

                productAmount.setText(baskets.get(position).getProductsCount() + " TOODET");
                productPriceRange.setText(baskets.get(position).getAllProductsPriceRange());


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
