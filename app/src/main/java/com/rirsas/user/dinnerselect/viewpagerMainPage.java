package com.rirsas.user.dinnerselect;

//import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by USER on 2016/06/19.
 */
public class viewpagerMainPage extends Fragment {

    Button button;
    TextView txtResult;
    DBAdapter dbAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.viewpager_mainpage,null);

        dbAdapter = MainActivity.dbAdapter;

        button = (Button)view.findViewById(R.id.btnSelect);
        txtResult = (TextView)view.findViewById(R.id.txtResult);

        dbAdapter.open();

        if(dbAdapter.GetRecordCount()==0){
            AtOnce(dbAdapter);
        }
        Cursor c = dbAdapter.GetAllRecord();
        final ArrayList<String> arrayList = new ArrayList<String>();

        if(c.moveToFirst()){
            do {
                arrayList.add(c.getString(c.getColumnIndex("genre_name")));
            }while(c.moveToNext());
        }

        dbAdapter.close();

        // Select Result Text from ResultText Array
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> aList = DBtoList(dbAdapter,arrayList);

                String strResultText;

                Collections.shuffle(aList);
                strResultText = aList.get(0);

                txtResult.setText(strResultText);
            }
        });

        return view;
    }

    public void onViewCreated(View view,Bundle saverInstanceState){
        super.onViewCreated(view,saverInstanceState);

    }

    //At Once
    private void AtOnce(DBAdapter dbadapter){
        dbadapter.InsertRecord("ラーメン");
        dbadapter.InsertRecord("丼物");
        dbadapter.InsertRecord("餃子");
        dbadapter.InsertRecord("肉料理");
        dbadapter.InsertRecord("パスタ");
    }

    private ArrayList<String> DBtoList(DBAdapter dbAdapter,ArrayList<String> arrayList){

        arrayList.clear();

        dbAdapter.open();
        if(!(dbAdapter.GetRecordCount()==0)){
            Cursor c = dbAdapter.GetAllRecord();

            if(c.moveToFirst()){
                do {
                    arrayList.add(c.getString(c.getColumnIndex("genre_name")));
                }while(c.moveToNext());
            }
        }

        dbAdapter.close();

        return arrayList;
    }

}
