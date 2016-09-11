package com.nutikorv.andreas.nutikorvalpha.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.nutikorv.andreas.nutikorvalpha.Objects.Basket;
import com.nutikorv.andreas.nutikorvalpha.Objects.Product;
import com.nutikorv.andreas.nutikorvalpha.R;

import java.util.List;

/**
 * Created by ANDREAS on 10.09.2016.
 */
public class BasketListView extends BaseAdapter {

    private List<Basket> baskets;
    private Context mContext;
    private String letter;
    private ColorGenerator generator = ColorGenerator.MATERIAL;
    private int mExpandedPosition = -1;
    private ListView r1;


    public BasketListView(Context context, List<Basket> baskets, ListView r1) {
        this.baskets = baskets;
        this.mContext = context;
        this.r1 = r1;
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {
//        Basket b = getItem(position);

        CustomViewHolder holder;

        if (convertView == null) {

            convertView = new LinearLayout(mContext);
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(inflater);
            convertView = vi.inflate(R.layout.basket_menu_item, parent, false);

            holder = new CustomViewHolder();




            //
            convertView.setTag(holder);
        }
        else{
            holder = (CustomViewHolder) convertView.getTag();
        }



        holder.imageLetter = (ImageView) convertView.findViewById(R.id.item_letter);
        holder.basketName = (TextView) convertView.findViewById(R.id.item_title);
        holder.expandedLayout = (RelativeLayout) convertView.findViewById(R.id.expandedLayout);
        holder.productAmount = (TextView) convertView.findViewById(R.id.productAmountText);
        holder.productPriceRange = (TextView) convertView.findViewById(R.id.priceRangeText);

        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "OpenSans-Semibold.ttf");

        String basketName = getItem(position).getBasketName();

        holder.basketName.setText(basketName);
        holder.basketName.setTypeface(font);

        letter = "" + basketName.charAt(0);

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(letter, generator.getColor(basketName));


        holder.imageLetter.setImageDrawable(drawable);

        holder.expandedLayout.setVisibility(View.VISIBLE);



//        holder.productAmount.setText(baskets.get(position).getProductsCount() + " TOODET");
//        holder.productPriceRange.setText(baskets.get(position).getAllProductsPriceRange());

        final boolean isExpanded = position == mExpandedPosition;
        holder.expandedLayout.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        convertView.setActivated(isExpanded);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1:position;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(parent);
                }
                notifyDataSetChanged();
            }
        });



        return convertView;
    }

    @Override
    public int getCount() {
        return baskets.size();
    }

    @Override
    public Basket getItem(int position) {
        return baskets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class CustomViewHolder {
        protected ImageView imageLetter;
        protected TextView basketName;
        protected RelativeLayout expandedLayout;
        protected TextView productAmount;
        protected TextView productPriceRange;
    }


}
