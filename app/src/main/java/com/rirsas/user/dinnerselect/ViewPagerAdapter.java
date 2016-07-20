package com.rirsas.user.dinnerselect;

import com.rirsas.user.dinnerselect.viewpagerMainPage;
import com.rirsas.user.dinnerselect.viewpagerSubPage;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.VelocityTrackerCompat;

/**
 * Created by USER on 2016/06/19.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter{

    public ViewPagerAdapter(FragmentManager fm){
        super(fm);
    }


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

        switch(position){
            case 0:
                return "メインページ";
            default:
                return "サブページ";
        }

//        return "Page" + position;
    }
}
