package com.example.edward.tradingapp.Repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.edward.tradingapp.Models.Good;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 03-Feb-17.
 */

public class LocalDBHandler extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "GoodsApp.db";

    private static final String TABLE_Goods = "Goods";
    private static final String COLUMN_ID = "Id";
    private static final String COLUMN_NAME = "Name";
    private static final String COLUMN_QUANTITY = "Quantity";
    private static final String COLUMN_PRICE = "Price";
    private static final String COLUMN_UPDATED = "Updated";

    public LocalDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        String query = "CREATE TABLE " + TABLE_Goods + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_QUANTITY + " INTEGER, " +
                COLUMN_PRICE + " INTEGER, " +
                COLUMN_UPDATED + " INTEGER " +
                ");";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_Goods);
        onCreate(sqLiteDatabase);
    }

    public List<Good> GetGoods()
    {
        List<Good> notesList = new ArrayList<Good>();

        SQLiteDatabase db = getWritableDatabase();
        String querry = "SELECT * FROM " + TABLE_Goods + ";";

        Cursor c = db.rawQuery(querry,null);

        c.moveToFirst();

        while (!c.isAfterLast())
        {
            if(c.getString(c.getColumnIndex(COLUMN_NAME)) != null)
            {
                int id = c.getInt(c.getColumnIndex(COLUMN_ID));
                String text = c.getString(c.getColumnIndex(COLUMN_NAME));
                int date  = c.getInt(c.getColumnIndex(COLUMN_QUANTITY));
                int version =  c.getInt(c.getColumnIndex(COLUMN_PRICE));
                int updated = c.getInt(c.getColumnIndex(COLUMN_UPDATED));

                notesList.add(new Good(id,text,date,version,updated));
            }
            c.moveToNext();
        }

        db.close();

        return notesList;
    }


    public void AddGood(int id,String name,int quantity,int price,int updated)
    {
        ContentValues values = new ContentValues();

        values.put(COLUMN_ID,id);
        values.put(COLUMN_NAME,name);
        values.put(COLUMN_QUANTITY,quantity);
        values.put(COLUMN_PRICE,price);
        values.put(COLUMN_UPDATED,updated);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_Goods,null,values);
        db.close();
    }

    public void AddGood(Good good)
    {
        AddGood(good.getId(),good.getName(),good.getQuantity(),good.getPrice(),good.getUpdated());
    }

    public void DeleteGoodById(int id)
    {

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM  " + TABLE_Goods + " WHERE " + COLUMN_ID + "=" + id + ";");

    }

    public void UpdateGoodById(int id,String name,int quantity,int price,int updated)
    {
        String query = "UPDATE " + TABLE_Goods + " SET " +
                COLUMN_NAME + "=\'" + name +"\', " +
                COLUMN_QUANTITY + "=" + quantity +", " +
                COLUMN_UPDATED + "=" + updated +", " +
                COLUMN_PRICE + "=" + price + " WHERE " +
                COLUMN_ID + "=" + id + ";";

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);

    }

    public void UpdateGoodById(Good good)
    {
        UpdateGoodById(good.getId(),good.getName(),good.getQuantity(),good.getPrice(),good.getUpdated());
    }

    public String databaseToString()
    {
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String querry = "SELECT * FROM " + TABLE_Goods;

        Cursor c = db.rawQuery(querry,null);

        c.moveToFirst();

        while (!c.isAfterLast())
        {
            if(c.getString(c.getColumnIndex(COLUMN_NAME)) != null)
            {
                dbString += c.getString(c.getColumnIndex(COLUMN_ID));
                dbString += ",";
                dbString += c.getString(c.getColumnIndex(COLUMN_NAME));
                dbString += ",";
                dbString += c.getString(c.getColumnIndex(COLUMN_QUANTITY));
                dbString += ",";
                dbString += c.getString(c.getColumnIndex(COLUMN_PRICE));
                dbString += ",";
                dbString += c.getString(c.getColumnIndex(COLUMN_UPDATED));
                dbString += "@";
            }
            c.moveToNext();
        }

        db.close();
        return dbString;

    }
}