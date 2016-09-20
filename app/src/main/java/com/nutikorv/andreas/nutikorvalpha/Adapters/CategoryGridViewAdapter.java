package com.nutikorv.andreas.nutikorvalpha.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nutikorv.andreas.nutikorvalpha.Objects.MainCategory;
import com.nutikorv.andreas.nutikorvalpha.R;

import java.util.List;

/**
 * Created by ANDREAS on 28.08.2016.
 */
public class CategoryGridViewAdapter extends BaseAdapter {

    private Context mContext;


    public CategoryGridViewAdapter(Context c) {
        mContext = c;

    }

    public int getCount() {
        return 20;
    }

    public Integer getItem(int position) {
        return 1;
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
            gridView = inflater.inflate(R.layout.category_item_layout, null);

            TextView textView = (TextView) gridView
                    .findViewById(R.id.grid_item_name);


        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }

}