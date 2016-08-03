package com.nutikorv.andreas.nutikorvalpha.Adapters;

/**
 * Created by ANDREAS on 13.07.2016.
 */
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.nutikorv.andreas.nutikorvalpha.Objects.MainCategory;
import com.nutikorv.andreas.nutikorvalpha.Objects.Product;
import com.nutikorv.andreas.nutikorvalpha.R;

import java.util.List;
import java.util.Map;

public class ExpandableAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private Map<String, List<Product>> laptopCollections;
    private List<String> laptops;


    public ExpandableAdapter(Activity context, MainCategory category) {
        this.context = context;
        this.laptopCollections = category.getHashMapProducts();
        this.laptops = category.getSubCategoriesString();
    }

    public Product getChild(int groupPosition, int childPosition) {
        return laptopCollections.get(laptops.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String laptop = (String) getChild(groupPosition, childPosition).toString();
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_item, null);
        }


        TextView item = (TextView) convertView.findViewById(R.id.food);


        item.setText(laptop);

        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return laptopCollections.get(laptops.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition) {
        return laptops.get(groupPosition);
    }

    public int getGroupCount() {
        return laptops.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String laptopName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_item,
                    null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.food);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(laptopName);
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}