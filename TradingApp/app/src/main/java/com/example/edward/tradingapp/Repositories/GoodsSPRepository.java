package com.example.edward.tradingapp.Repositories;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.edward.tradingapp.Models.Good;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 03-Feb-17.
 */

public class GoodsSPRepository
{
    private String jsonString;
    private List<Good> goodsList;
    private Context context;

    public GoodsSPRepository() {}

    public GoodsSPRepository(Context context, List<Good> goodsList, String jsonString)
    {
        this.context = context;
        this.goodsList = goodsList;
        this.jsonString = jsonString;
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }

    public List<Good> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Good> goodsList) {
        this.goodsList = goodsList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private Boolean isCached()
    {
        SharedPreferences pref = this.context.getApplicationContext().getSharedPreferences("GoodsList", context.MODE_PRIVATE);

        return pref.contains("goodsListJSON") ? true : false;
    }

    private List<Good> GetGoods()
    {
        this.goodsList = new ArrayList<Good>();
        try {
            JSONArray jsonArr = new JSONArray(this.getJsonString());


            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject obj = jsonArr.getJSONObject(i);
                this.goodsList.add(new Good(obj.getInt("id"), obj.getString("name"), obj.getInt("quantity"), obj.getInt("price"),obj.getInt("updated")));
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        return this.goodsList;
    }





}
