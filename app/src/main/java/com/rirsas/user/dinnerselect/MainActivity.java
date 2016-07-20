package com.rirsas.user.dinnerselect;

import android.content.Context;
import android.support.v4.view.ViewPager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ViewPager viewpager;

    //static ArrayList<String> array = new ArrayList<>(Arrays.asList("ラーメン","餃子","丼物","肉"));
    static ArrayList<String> array = new ArrayList<>();
    static DBAdapter dbAdapter;
    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main_for_viewpager);

        dbAdapter = new DBAdapter(getApplicationContext());

        context = getApplicationContext();

        viewpager = (ViewPager) findViewById(R.id.pager);
        viewpager.setAdapter(
                new ViewPagerAdapter(
                        getSupportFragmentManager()));


    }
}

