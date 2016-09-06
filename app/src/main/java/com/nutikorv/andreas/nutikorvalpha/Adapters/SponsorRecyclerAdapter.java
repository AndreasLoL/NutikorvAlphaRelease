package com.nutikorv.andreas.nutikorvalpha.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.nutikorv.andreas.nutikorvalpha.Objects.OnSaleProduct;
import com.nutikorv.andreas.nutikorvalpha.R;

import java.util.List;

/**
 * Created by ANDREAS on 27.08.2016.
 */
public class SponsorRecyclerAdapter extends RecyclerView.Adapter<SponsorRecyclerAdapter.CustomViewHolder> {
    private Context mContext;
    private List<OnSaleProduct> onSaleProducts;

    public SponsorRecyclerAdapter(Context context, List<OnSaleProduct> onSaleProducts) {
        this.mContext = context;
        this.onSaleProducts = onSaleProducts;

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sales_cardview_item_new, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SponsorRecyclerAdapter.CustomViewHolder holder, int position) {

        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "OpenSans-Light.ttf");

        Typeface font2 = Typeface.createFromAsset(mContext.getAssets(), "OpenSans-Semibold.ttf");

        holder.productName.setText(onSaleProducts.get(position).getName());

        holder.productName.setTypeface(font2);

        holder.productPrice.setTypeface(font2);

        holder.productPrice.setText(onSaleProducts.get(position).getFormatedCurrentPrice());

        UrlImageViewHelper.setUrlDrawable(holder.productImage, onSaleProducts.get(position).getImgURL());

        holder.productOldPrice.setPaintFlags(holder.productOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        holder.productOldPrice.setTypeface(font);

        holder.productOldPrice.setText(onSaleProducts.get(position).getFormatedOldPrice());

//        holder.shopName.setText(onSaleProducts.get(position).getShopName());
//
//        holder.shopName.setTextColor(Color.BLUE);

        UrlImageViewHelper.setUrlDrawable(holder.shopName, onSaleProducts.get(position).getShopLogoURL());

        holder.saleDuration.setText(onSaleProducts.get(position).getDuration());

        holder.salePercentage.setText(onSaleProducts.get(position).getSalePercentage());


    }

    @Override
    public int getItemCount() {
        return onSaleProducts.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView productPrice;
        protected ImageView productImage;
        protected TextView productOldPrice;
        protected TextView productName;
        protected ImageView shopName;
        protected TextView saleDuration;
        protected TextView salePercentage;

        public CustomViewHolder(View view) {
            super(view);

            productName = (TextView) view
                    .findViewById(R.id.gridName);

            productOldPrice = (TextView) view.findViewById(R.id.gridOldPrice);

            productImage = (ImageView) view.findViewById(R.id.gridImage);

            productPrice = (TextView) view.findViewById(R.id.gridPrice);

            shopName = (ImageView) view.findViewById(R.id.shopName);

            saleDuration = (TextView) view.findViewById(R.id.saleDuration);

            salePercentage = (TextView) view.findViewById(R.id.salePercentage);


        }
    }
}



