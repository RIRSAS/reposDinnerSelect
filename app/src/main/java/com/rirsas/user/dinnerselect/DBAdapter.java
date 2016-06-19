package com.rirsas.user.dinnerselect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

        Date dateNow = new Date();
        ContentValues values = new ContentValues();
        values.put("genre_name",genre_name);
        values.put("created",yyyyMMddhhmm.format(dateNow));
        db.insertOrThrow(TABLE_NAME,null,values);
    }

}
