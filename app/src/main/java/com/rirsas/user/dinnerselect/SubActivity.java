package com.rirsas.user.dinnerselect;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

//For Setting
public class SubActivity extends AppCompatActivity {

    Button buttonBackToMain;
    Button buttonAddMenu;
    EditText txtAddItem;
    ListView lvMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        buttonBackToMain = (Button)findViewById(R.id.btnBackToMain);
        buttonAddMenu = (Button)findViewById(R.id.btnAddMenu);
        txtAddItem = (EditText)findViewById(R.id.editText);
        lvMenu = (ListView)findViewById(R.id.lvMenu);
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        txtAddItem.setText("");

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1);

        final DBAdapter dbA = MainActivity.dbAdapter;

        final ArrayList<String> aList = new ArrayList<String>();

        UpdateList(dbA,adapter);

        /*
        dbA.open();

        Cursor c = dbA.GetAllRecord();

        if(c.moveToFirst()){
            do{
                adapter.add(c.getString(c.getColumnIndex("genre_name")));
            }while(c.moveToNext());
        }

        */

        lvMenu.setAdapter(adapter);

        /*
                Cursor c = dbAdapter.GetAllRecord();
        final ArrayList<String> arrayList = new ArrayList<String>();

        if(c.moveToFirst()){
            do {
                 arrayList.add(c.getString(c.getColumnIndex("genre_name")));
            }while(c.moveToNext());
        }

        */

        lvMenu.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent,View view,int position, long id){
                alert.setTitle("削除確認");

                final String text_target = lvMenu.getItemAtPosition(position).toString();

                alert.setMessage("「" + text_target + "」を削除してもよろしいですか？");

                // "Yes" Button Works
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbA.open();

                        // Must 1 Menu Exist
                        if(dbA.GetRecordCount() != 1){
                            dbA.DeleteTargetRecord(text_target);
                        }else{
                            AlertDialog.Builder child_alert = new AlertDialog.Builder(SubActivity.this);
                            child_alert.setTitle("アラート");
                            child_alert.setMessage("メニューは１つ以上必要です");
                            child_alert.setPositiveButton("OK",null);
                            child_alert.show();
                        }

                        dbA.close();

                        // Update List
                        UpdateList(dbA,adapter);
                    }
                });

                // "No" Button Works
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alert.show();

                return false;
            }
        });

        //Return to MainActivity
        buttonBackToMain.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });

        //Add New Menu
        buttonAddMenu.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                ArrayList<String> tmpArray = MainActivity.array;
                //DBAdapter dbAdapter = MainActivity.dbAdapter;
                String strItem;

                strItem = txtAddItem.getText().toString();

                //Check Text
                if (!strItem.isEmpty()) {
                    // Set to Array 20160609 chg
                    //tmpArray.add(strItem);
                    dbA.open();
                    dbA.InsertRecord(strItem);
                    dbA.close();
                    UpdateList(dbA,adapter);
                    txtAddItem.setText("");
                } else {
                    Toast.makeText(SubActivity.this, "Please Set Any Text!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //set food name to db
    }

    // リストの表示内容を更新
    private void UpdateList(DBAdapter dbadapter,ArrayAdapter<String> adapter) {
        dbadapter.open();
        Cursor c = dbadapter.GetAllRecord();

        // リスト内容の初期化
        adapter.clear();

        // 先頭カーソルから順次リストへ追加
        if(c.moveToFirst()){
            do{
                adapter.add(c.getString(c.getColumnIndex("genre_name")));
            }while(c.moveToNext());}

        // ListViewへ更新を通知
        adapter.notifyDataSetChanged();

        // DBのクローズ
        dbadapter.close();
    }
}
