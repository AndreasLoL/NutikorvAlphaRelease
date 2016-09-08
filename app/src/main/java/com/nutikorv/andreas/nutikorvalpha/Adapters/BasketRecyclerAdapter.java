package com.nutikorv.andreas.nutikorvalpha.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.nutikorv.andreas.nutikorvalpha.Parameters.GlobalParameters;
import com.nutikorv.andreas.nutikorvalpha.R;

/**
 * Created by AndreasPC on 9/8/2016.
 */
public class BasketRecyclerAdapter extends RecyclerView.Adapter<BasketRecyclerAdapter.CustomViewHolder> {
    private String letter;
    private Context mContext;
    private ColorGenerator generator = ColorGenerator.MATERIAL;

    public BasketRecyclerAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.basket_menu_item, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BasketRecyclerAdapter.CustomViewHolder holder, int position) {

        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "OpenSans-Light.ttf");

        holder.basketName.setText("BASKET NAME");
        letter = "B"; // First letter of basket name!

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(letter, generator.getRandomColor());

        holder.imageLetter.setImageDrawable(drawable);


    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageLetter;
        protected TextView basketName;

        public CustomViewHolder(View view) {
            super(view);

            imageLetter = (ImageView) view.findViewById(R.id.item_letter);
            basketName = (TextView) view.findViewById(R.id.item_title);

        }
    }
}
