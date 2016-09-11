package com.nutikorv.andreas.nutikorvalpha.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.nutikorv.andreas.nutikorvalpha.Objects.Basket;
import com.nutikorv.andreas.nutikorvalpha.Parameters.GlobalParameters;
import com.nutikorv.andreas.nutikorvalpha.R;

import java.util.List;

/**
 * Created by AndreasPC on 9/8/2016.
 */
public class BasketRecyclerAdapter extends RecyclerView.Adapter<BasketRecyclerAdapter.CustomViewHolder> {
    private String letter;
    private Context mContext;
    private ColorGenerator generator = ColorGenerator.MATERIAL;
    private List<Basket> baskets;
    private int mExpandedPosition = -1;
    private RecyclerView r1;

    public BasketRecyclerAdapter(Context context, List<Basket> baskets, RecyclerView r1) {
        this.mContext = context;
        this.baskets = baskets;
        this.r1 = r1;

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.basket_menu_item, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final BasketRecyclerAdapter.CustomViewHolder holder, final int position) {

        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "OpenSans-Semibold.ttf");

        String basketName = baskets.get(position).getBasketName();

        holder.basketName.setText(basketName);
        holder.basketName.setTypeface(font);

        letter = "" + basketName.charAt(0);

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(letter, generator.getColor(basketName));


        holder.imageLetter.setImageDrawable(drawable);


//        holder.productAmount.setText(baskets.get(position).getProductsCount() + " TOODET");
//        holder.productPriceRange.setText(baskets.get(position).getAllProductsPriceRange());

        final boolean isExpanded = position == mExpandedPosition;
        holder.expandedLayout.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.itemView.setActivated(isExpanded);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1:position;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(r1);
                }
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return baskets.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageLetter;
        protected TextView basketName;
        protected RelativeLayout expandedLayout;
        protected TextView productAmount;
        protected TextView productPriceRange;

        public CustomViewHolder(View view) {
            super(view);
            imageLetter = (ImageView) view.findViewById(R.id.item_letter);
            basketName = (TextView) view.findViewById(R.id.item_title);
            expandedLayout = (RelativeLayout) view.findViewById(R.id.expandedLayout);
//            productAmount = (TextView) view.findViewById(R.id.productAmountText);
//            productPriceRange = (TextView) view.findViewById(R.id.priceRangeText);

        }
    }
}
