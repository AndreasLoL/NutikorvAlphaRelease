package com.nutikorv.andreas.nutikorvalpha.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.nutikorv.andreas.nutikorvalpha.Objects.MainCategory;
import com.nutikorv.andreas.nutikorvalpha.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by ANDREAS on 27.08.2016.
 */
public class SalesGridViewAdapter extends BaseAdapter {
    private Context mContext;

    private List<MainCategory> mainCategories;

    public SalesGridViewAdapter(Context c) {
        mContext = c;

    }

    public int getCount() {
        return 20;
    }

    public MainCategory getItem(int position) {
        return mainCategories.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(mContext);

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.sales_cardview_item, null);

            Typeface font = Typeface.createFromAsset(mContext.getAssets(), "OpenSans-Light.ttf");

            TextView productName = (TextView) gridView
                    .findViewById(R.id.gridName);

            TextView productOldPrice = (TextView) gridView.findViewById(R.id.gridOldPrice);

            ImageView productImage = (ImageView) gridView.findViewById(R.id.gridImage);

            TextView productPrice = (TextView) gridView.findViewById(R.id.gridPrice);


            productName.setText("See on test toode väga väga väga väga pika nimega!");

            productName.setTypeface(font);

            productPrice.setTypeface(font);



            UrlImageViewHelper.setUrlDrawable(productImage, "https://s3-eu-west-1.amazonaws.com/balticsimages/images/180x220/47dcd66548f24b87f7851f03663f7dc1.png");

            productPrice.setText("15.99€");

            productOldPrice.setPaintFlags(productOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            productOldPrice.setTypeface(font);

        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }

}
