package com.nutikorv.andreas.nutikorvalpha.Adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nutikorv.andreas.nutikorvalpha.Objects.MainCategory;
import com.nutikorv.andreas.nutikorvalpha.R;

import java.util.List;

/**
 * Created by ANDREAS on 26.08.2016.
 */
public class GridViewAdapter extends BaseAdapter {

    private Context mContext;

    private List<MainCategory> mainCategories;

    public GridViewAdapter(Context c, List<MainCategory> mainCategories) {
        mContext = c;
        this.mainCategories = mainCategories;

    }

    public int getCount() {
        return mainCategories.size();
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
            gridView = inflater.inflate(R.layout.gridview_item, null);

            TextView textView = (TextView) gridView
                    .findViewById(R.id.grid_item_name);
            textView.setText(mainCategories.get(position).getName());


        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }

}
