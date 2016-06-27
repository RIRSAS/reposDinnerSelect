package com.rirsas.user.dinnerselect;

//import android.app.Fragment;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by USER on 2016/06/19.
 */
public class viewpagerSubPage extends Fragment {

    Button buttonAddMenu;
    EditText txtAddItem;
    ListView lvMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.viewpager_subpage,null);

        buttonAddMenu = (Button)view.findViewById(R.id.btnAddMenu);
        txtAddItem = (EditText)view.findViewById(R.id.editText);
        lvMenu = (ListView)view.findViewById(R.id.lvMenu);

//        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.context);
        final AlertDialog.Builder alert = new AlertDialog.Builder(viewpagerSubPage.this.getActivity());

        txtAddItem.setText("");

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(
                // MainActivity.context, android.R.layout.simple_list_item_1);
                viewpagerSubPage.this.getActivity(), android.R.layout.simple_list_item_1);

        final DBAdapter dbA = MainActivity.dbAdapter;

        // ビューを形成したタイミングでリストビューの表示内容を更新する
        UpdateList(dbA,adapter);

        // リストアダプターをビューへセット
        lvMenu.setAdapter(adapter);
        
        // リスト上のメニュー名を長押しした場合の削除処理
        lvMenu.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
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
                            AlertDialog.Builder child_alert = new AlertDialog.Builder(viewpagerSubPage.this.getActivity());
                            child_alert.setTitle("アラート");
                            // メニューを0個にしようとした
                            child_alert.setMessage(getString(R.string.alert_need_one));
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
                    if(dbA.CheckInsertable(strItem)){
                        dbA.InsertRecord(strItem);
                    } else {
                        AlertDialog.Builder child_alert = new AlertDialog.Builder(viewpagerSubPage.this.getActivity());
                        child_alert.setTitle("アラート");
                        // 登録対象重複アラート
                        child_alert.setMessage(getString(R.string.alert_duplicate));
                        child_alert.setPositiveButton("OK",null);
                        child_alert.show();
                    }
                    dbA.close();
                    UpdateList(dbA,adapter);
                    txtAddItem.setText("");
                } else {
                    AlertDialog.Builder child_alert = new AlertDialog.Builder(viewpagerSubPage.this.getActivity());
                    child_alert.setTitle("アラート");
                    // 空欄登録アラート
                    child_alert.setMessage(getString(R.string.alert_empty));
                    child_alert.setPositiveButton("OK",null);
                    child_alert.show();
                }
            }
        });

        return view;
    }

    public void onViewCreated(View view,Bundle saverInstanceState){
        super.onViewCreated(view,saverInstanceState);

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
