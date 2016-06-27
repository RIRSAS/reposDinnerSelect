package com.rirsas.user.dinnerselect;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by USER on 2016/06/27.
 */
public class CustomCheckAdapter extends ArrayAdapter<MenuState>{

    private ArrayList<MenuState> menulist;

    public CustomCheckAdapter(Context context, int textViewResourceId, ArrayList<MenuState> menulist){
        super(context, textViewResourceId, menulist);
        this.menulist = new ArrayList<MenuState>();
        this.addAll(menulist);
    }


}
