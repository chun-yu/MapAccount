package com.chun.mapaccount;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rmp4m on 2018/1/2.
 */

public class MyDB extends SQLiteOpenHelper {
    private final static int _DBVersion = 1; //<-- 版本
    private final static String _DBName = "SampleList.db";  // db name
    private final static String TABLE_NAME1 = "MyCoast"; // table name
    private final static String TABLE_NAME2 = "MyIncome";
    private final static String TABLE_NAME3 = "MyItem";

    public MyDB(Context context) {
        super(context, _DBName, null, _DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String INIT_TABLE1 = "CREATE TABLE " + TABLE_NAME1 + " (" +
                "ID   INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "DATE  TEXT(10), " +
                "COAST REAL(20) NOT NULL, " +
                "ITEM TEXT(20), " +
                "LATITUDE REAL, " +
                "LONGITUDE REAL," +
                "ADDRESS TEXT(30),"+
                "PLACENAME TEXT(30) );";
        final String INIT_TABLE2 = "CREATE TABLE " + TABLE_NAME2 + " (" +
                "ID   INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "DATE  TEXT(10), " +
                "COAST REAL(20) NOT NULL, " +
                "ITEM TEXT(20) );";
        final String INIT_TABLE3 = "CREATE TABLE " + TABLE_NAME3 + " (" +
                "ID   INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "CLASS  TEXT(10), " +
                "ITEM TEXT(20) );";
        sqLiteDatabase.execSQL(INIT_TABLE1);
        sqLiteDatabase.execSQL(INIT_TABLE2);
        sqLiteDatabase.execSQL(INIT_TABLE3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        final String SQL1 = "DROP TABLE " + TABLE_NAME1;
        final String SQL2 = "DROP TABLE " + TABLE_NAME2;
        final String SQL3 = "DROP TABLE " + TABLE_NAME3;
        sqLiteDatabase.execSQL(SQL1);
        sqLiteDatabase.execSQL(SQL2);
        sqLiteDatabase.execSQL(SQL3);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public synchronized void close() {
        super.close();
    }

    public boolean delete_coast(long rowId) { //刪除指定的資料
        return getWritableDatabase().delete(TABLE_NAME1, "ID" + "=" + rowId, null) > 0;
    }

    public boolean delete_income(long rowId) { //刪除指定的資料
        return getWritableDatabase().delete(TABLE_NAME2, "ID" + "=" + rowId, null) > 0;
    }

    public boolean delete_item(long rowId) { //刪除指定的資料
        return getWritableDatabase().delete(TABLE_NAME3, "ID" + "=" + rowId, null) > 0;
    }

    public void addCoast(String Date, double Coast, String Item, double Latitude, double Longitude,String Address,String Place) {
        ContentValues values = new ContentValues();
        values.put("DATE", Date.toString());
        values.put("COAST", Coast);
        values.put("ITEM", Item.toString());
        values.put("LATITUDE", Latitude);
        values.put("LONGITUDE", Longitude);
        values.put("ADDRESS", Address);
        values.put("PLACENAME", Place);
        getWritableDatabase().insert(TABLE_NAME1, null, values);
    }

    public void addIncome(String Date, double Coast, String Item) {
        ContentValues values = new ContentValues();
        values.put("DATE", Date.toString());
        values.put("COAST", Coast);
        values.put("ITEM", Item.toString());
        getWritableDatabase().insert(TABLE_NAME2, null, values);
    }

    public void addItem(String item_class, String Item) {
        ContentValues values = new ContentValues();
        values.put("CLASS", item_class);
        values.put("ITEM", Item);
        getWritableDatabase().insert(TABLE_NAME3, null, values);
    }

    public Cursor getCoastData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME1, null);
        return res;
    }

    public Cursor getIncomeData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME2, null);
        return res;
    }

    public Cursor getItemData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME3, null);
        return res;
    }

    public boolean updateCoastData(long rowId, String Date, double Coast, String Item, double Latitude, double Longitude) {
        ContentValues values = new ContentValues();
        values.put("DATE", Date.toString());
        values.put("COAST", Coast);
        values.put("ITEM", Item.toString());
        values.put("LATITUDE", Latitude);
        values.put("LONGITUDE", Longitude);
        getWritableDatabase().update(TABLE_NAME1, values, "ID" + "=" + rowId, null);
        return true;
    }

    public boolean updateIncomeData(long rowId, String Date, double Coast, String Item) {
        ContentValues values = new ContentValues();
        values.put("DATE", Date.toString());
        values.put("COAST", Coast);
        values.put("ITEM", Item.toString());
        getWritableDatabase().update(TABLE_NAME1, values, "ID" + "=" + rowId, null);
        return true;
    }

    public boolean updateItemData(long rowId, String item_class, String item) {
        ContentValues values = new ContentValues();
        values.put("ID", rowId);
        values.put("CLASS", item_class);
        values.put("ITEM", item);
        getWritableDatabase().update(TABLE_NAME3, values, "ID=" + rowId, null);
        return true;
    }

    public int getDBcount() {
        int result = 0;
        Cursor cursor = getWritableDatabase().rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME1, null);
        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }
        return result;
    }

    public int getDBcount2() {
        int result = 0;
        Cursor cursor = getWritableDatabase().rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME2, null);
        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }
        return result;
    }

    public void storyDB() {
        String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME2;
        getWritableDatabase().execSQL(DROP_TABLE);
    }
}
