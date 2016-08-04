package com.nutikorv.andreas.nutikorvalpha.Objects;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

/**
 * Created by ANDREAS on 04.08.2016.
 */
public class SubcategoryParentListItem implements ParentListItem {

    public String mTitle;
    private List<Product> mChildItemList;

    public SubcategoryParentListItem(SubCategory s) {
        mTitle = s.getName();

    }

    @Override
    public List<Product> getChildItemList() {
        return mChildItemList;
    }

    public void setChildItemList(List<Product> list) {
        mChildItemList = list;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

}
