package com.nutikorv.andreas.nutikorvalpha.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nutikorv.andreas.nutikorvalpha.Fragments.InnerBasketFragment;
import com.nutikorv.andreas.nutikorvalpha.Fragments.InnerCategoryFragment;
import com.nutikorv.andreas.nutikorvalpha.Fragments.InnerMainFragment;
import com.nutikorv.andreas.nutikorvalpha.Fragments.InstancedProductDisplayFragment;
import com.nutikorv.andreas.nutikorvalpha.Objects.BasketStorage;
import com.nutikorv.andreas.nutikorvalpha.Parameters.GlobalParameters;

import java.util.HashMap;

/**
 * Created by ANDREAS on 03.07.2016.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    FragmentManager mFragmentManager;

    HashMap<Integer, String> mFragmentTags;

    private Context mContext;


    public ViewPagerAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        mFragmentTags = new HashMap<Integer,String>();
        mFragmentManager = fm;
        this.mContext = mContext;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new InnerCategoryFragment();
            case 1:
                return new InnerMainFragment();
            case 2:
                Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                SharedPreferences sharedPref = mContext.getSharedPreferences(
                        GlobalParameters.BASKETS_PREFERENCE_SELECTED, Context.MODE_PRIVATE);
                BasketStorage basketStorage = gson.fromJson(sharedPref.getString(GlobalParameters.BASKETS_PREFERENCE,
                        null), BasketStorage.class);
                return new InstancedProductDisplayFragment().newInstance(gson.toJson(gson.toJson(basketStorage.getSelectedBasket())));
            default:
                break;
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object obj = super.instantiateItem(container, position);
        if (obj instanceof Fragment) {
            Fragment f = (Fragment) obj;
            String tag = f.getTag();
            mFragmentTags.put(position, tag);
        }
        return obj;
    }

    public Fragment getFragment(int position) {
        String tag = mFragmentTags.get(position);
        if (tag == null)
            return null;
        return mFragmentManager.findFragmentByTag(tag);
    }

}
