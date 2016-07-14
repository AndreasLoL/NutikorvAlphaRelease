package com.nutikorv.andreas.nutikorvalpha;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.HashMap;

/**
 * Created by ANDREAS on 03.07.2016.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    FragmentManager mFragmentManager;

    HashMap<Integer, String> mFragmentTags;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragmentTags = new HashMap<Integer,String>();
        mFragmentManager = fm;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new InnerMainFragment();
            case 1:
                return new InnerBasketFragment();
            default:
                break;
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object obj = super.instantiateItem(container, position);
        if (obj instanceof Fragment) {
            // record the fragment tag here.
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
