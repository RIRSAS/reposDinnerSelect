package com.rirsas.user.dinnerselect;

//import android.content.ContentValues;
import android.content.Intent;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    Button button;
    Button buttonMoveToSetting;
    TextView txtResult;
    static ArrayList<String> array = new ArrayList<>(Arrays.asList("ラーメン","餃子","丼物","肉"));
    static DBAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbAdapter = new DBAdapter(getApplicationContext());
        //final SQLiteDatabase db = helper.getWritableDatabase();

        button = (Button)findViewById(R.id.btnSelect);
        buttonMoveToSetting = (Button)findViewById(R.id.btnMoveSetting);
        txtResult = (TextView)findViewById(R.id.txtResult);

        dbAdapter.open();
//        dbAdapter.InsertRecord("ラーメン");
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

                //DBtoList(dbAdapter,arrayList);

                String strResultText;

                // Get Text at random
                //Collections.shuffle(array);
                //strResultText = array.get(0);

        /*
                Collections.shuffle(arrayList);
                strResultText = arrayList.get(0);
        */

                Collections.shuffle(aList);
                strResultText = aList.get(0);

                txtResult.setText(strResultText);
            }
        });

        // Move To Sub Activity
        buttonMoveToSetting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SubActivity.class);
                startActivity(intent);
            }
        });

        //get all item to Array

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

