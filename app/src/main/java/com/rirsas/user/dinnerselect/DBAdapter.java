package com.rirsas.user.dinnerselect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by USER on 2016/05/08.
 */

public class DBAdapter{

    public static final String TABLE_NAME = "genres";

    protected final Context context;
    protected DataBaseHelper dbHelper;
    protected SQLiteDatabase db;

    public DBAdapter(Context context){
        this.context = context;
        dbHelper = new DataBaseHelper(this.context);
    }

    private static class DataBaseHelper extends SQLiteOpenHelper {
        public DataBaseHelper(Context context) {
            super(context, "Menu.db", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //create new database then not exist database file
            db.execSQL("CREATE TABLE " + TABLE_NAME +
                    "(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    ", genre_name TEXT NOT NULL" +
                    ", weight INTEGER NOT NULL" +
                    ", created TEXT NOT NULL" +
                    ", modified TEXT " +
                    ")"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int OldVersion, int NewVersion) {
        }
    }

    //Adapter Method Open Database
    public DBAdapter open(){
        db = dbHelper.getWritableDatabase();
        return this;
    }

    //Adapter Method Close Database
    public void close() {
        dbHelper.close();
    }

    //App Method
    public Cursor GetAllRecord(){
        return db.query(TABLE_NAME,null,null,null,null,null,null );
    }

    public long GetRecordCount() {
        return DatabaseUtils.queryNumEntries(db,TABLE_NAME);
    }

    public boolean DeleteAllRecord(){
        return db.delete(TABLE_NAME,null,null) > 0;
    }

    public void DeleteTargetRecord(String gname) {
        // Begin Transaction Declaration
        db.beginTransaction();
        try{
            db.delete(TABLE_NAME,"genre_name=?",new String[]{gname});

            // Transaction Successful Finished
            db.setTransactionSuccessful();

        }finally{
            // Transaction Finish Declaration
            db.endTransaction();
        }
    }

    public void InsertRecord(String genre_name){

        DateFormat yyyyMMddhhmm = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        boolean flgDuplicate = false;

        Cursor c = GetAllRecord();

        if(c.moveToFirst()){
            do{
                if(c.getString(c.getColumnIndex("genre_name")).equals(genre_name)){
                    flgDuplicate = true;
                }
            }while(c.moveToNext());}

        if(flgDuplicate==false){
            Date dateNow = new Date();
            ContentValues values = new ContentValues();
            values.put("genre_name",genre_name);
            values.put("weight",5);
            values.put("created",yyyyMMddhhmm.format(dateNow));
            db.insertOrThrow(TABLE_NAME,null,values);
        }

    }

    // メニュー選出後の重み更新 選択された:重みを減らす 選択されなかった:重みを増やす
    public void UpdateWeight(String genre_name){

        open();

        Cursor c = db.query(TABLE_NAME,null,"genre_name = ?", new String[]{genre_name},null,null,null);

        int new_weight = 0;

        if(c.moveToFirst()){
           new_weight  = c.getInt(c.getColumnIndex("weight"));
        }

        String whereCaluse = "genre_name = ?";

        String whereArgs[] = new String[1];

        if(new_weight >= 2){

            // update対象用content
            ContentValues values = new ContentValues();

            // 重みを2減らす
            values.put("weight", new_weight -2);

            // 選択されたメニューが対象
            whereArgs[0] = genre_name;

            open();

            try {
                db.update(TABLE_NAME, values, whereCaluse, whereArgs);
            } finally {
                close();
            }

        }

        open();

        Cursor cUpdate = GetAllRecord();

        String weight_target;
        int weight_cnt_up;
        ContentValues values_cnt_up = new ContentValues();

        if(cUpdate.moveToFirst()) {

            try {

                do {
                    // 今回選択されなかったメニューすべて
                    if (!cUpdate.getString(cUpdate.getColumnIndex("genre_name")).equals(genre_name)) {

                        weight_target = cUpdate.getString(c.getColumnIndex("genre_name"));
                        weight_cnt_up = cUpdate.getInt(cUpdate.getColumnIndex("weight"));

                        // 重みを1増やす
                        values_cnt_up.put("weight", weight_cnt_up + 1);

                        whereArgs[0] = weight_target;

                        open();

                        db.update(TABLE_NAME, values_cnt_up, whereCaluse, whereArgs);

                    }
                } while (cUpdate.moveToNext());
            } finally {
                close();
            }
        }

    }

    public boolean CheckInsertable(String genre_name){

        boolean isInsertable = true;

        Cursor c = GetAllRecord();

        if(c.moveToFirst()){
            do{
                if(c.getString(c.getColumnIndex("genre_name")).equals(genre_name)){
                    isInsertable = false;
                }
            }while(c.moveToNext());}

        return isInsertable;

    }

}
