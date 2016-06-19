package com.rirsas.user.dinnerselect;

import com.rirsas.user.dinnerselect.viewpagerMainPage;
import com.rirsas.user.dinnerselect.viewpagerSubPage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by USER on 2016/06/19.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter{
    public ViewPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int i){
        switch(i){
            case 0:
                return new viewpagerMainPage();
            default:
                return new viewpagerSubPage();
        }
    }

    @Override
    public int getCount(){
     return 2;
    }

    @Override
    public CharSequence getPageTitle(int position){
        return "Page" + position;
    }
}
