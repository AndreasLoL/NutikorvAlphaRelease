package com.nutikorv.andreas.nutikorvalpha.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.nutikorv.andreas.nutikorvalpha.R;

/**
 * Created by ANDREAS on 28.08.2016.
 */
public class ShopRecyclerAdapter extends RecyclerView.Adapter<ShopRecyclerAdapter.CustomViewHolder> {
    private Context mContext;

    public ShopRecyclerAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shop_item, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ShopRecyclerAdapter.CustomViewHolder holder, int position) {

        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "OpenSans-Light.ttf");

//        holder.shopName.setText("  POE NIMI  ");

        holder.shopName.setTypeface(font);

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView shopName;

        public CustomViewHolder(View view) {
            super(view);

            shopName = (TextView) view.findViewById(R.id.shop_name);


        }
    }
}

