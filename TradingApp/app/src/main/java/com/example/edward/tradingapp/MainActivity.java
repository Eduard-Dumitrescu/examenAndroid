package com.example.edward.tradingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.edward.tradingapp.Models.Good;
import com.example.edward.tradingapp.Repositories.GoodsSPRepository;
import com.example.edward.tradingapp.Repositories.LocalDBHandler;
import com.example.edward.tradingapp.Services.GetGoodsService;
import com.example.edward.tradingapp.Services.SellGoodsService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private ListView listView;
    private ListView transactionsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("MainActivity","Main Activity created");

        listView = (ListView) findViewById(R.id.goodsList);
        transactionsListView = (ListView) findViewById(R.id.transactionsList);

        PopulateTransactions();

        GetGoodsService getGoods = new GetGoodsService(){
            @Override
            protected void onPostExecute(Object o) {
                try
                {
                    JSONArray jsonArr = new JSONArray(this.getMessage());
                    List<Good> goodsList = new ArrayList<Good>();

                    for (int i=0;i<jsonArr.length();i++)
                    {
                        JSONObject obj = jsonArr.getJSONObject(i);
                        goodsList.add(new Good(obj.getInt("id"),obj.getString("name"),obj.getInt("quantity"),obj.getInt("price"),obj.getInt("updated")));
                    }

                    PopulateList(goodsList);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        getGoods.execute();

        SellLocal();

    }

    private void PopulateList(final List<Good> goodsList)
    {
        List<String> goodsName = new ArrayList<String>();

        for (Good good : goodsList)
        {
            goodsName.add(good.getName());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                goodsName );

        listView.setAdapter(arrayAdapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent intent = new Intent(MainActivity.this,DetailGoodsActivity.class);

                intent.putExtra("Id", goodsList.get(i).getId());
                intent.putExtra("Name", goodsList.get(i).getName());
                intent.putExtra("Price", goodsList.get(i).getPrice());
                intent.putExtra("Quantity",goodsList.get(i).getQuantity());
                intent.putExtra("Updated",goodsList.get(i).getUpdated());
                intent.putExtra("IsBuy",true);
                intent.putExtra("Transaction",false);

                startActivity(intent);
            }

        });
    }

    private void PopulateTransactions()
    {


        GoodsSPRepository repo = new GoodsSPRepository(MainActivity.this);
        List<String> goodsName = new ArrayList<String>();

        final List<Good> goodsList = new ArrayList<>(repo.getGoodsList());

        for (Good good : goodsList) {
            goodsName.add(good.getName());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                goodsName);

        transactionsListView.setAdapter(arrayAdapter);

        transactionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent intent = new Intent(MainActivity.this,DetailGoodsActivity.class);

                intent.putExtra("Id", goodsList.get(i).getId());
                intent.putExtra("Name", goodsList.get(i).getName());
                intent.putExtra("Price", goodsList.get(i).getPrice());
                intent.putExtra("Quantity",goodsList.get(i).getQuantity());
                intent.putExtra("Updated",goodsList.get(i).getUpdated());
                intent.putExtra("Transaction",true);

                startActivity(intent);
            }

        });

    }

    public void GoToSellGood(View view)
    {
        Intent intent = new Intent(MainActivity.this,DetailGoodsActivity.class);

        intent.putExtra("IsBuy",false);
        intent.putExtra("Transaction",false);

        startActivity(intent);
    }

    public void SellLocal()
    {
        final LocalDBHandler repo = new LocalDBHandler(this,null,null,1);

        List<Good> goodsToSell = new ArrayList<>(repo.GetGoods());

        for (Good good: goodsToSell)
        {
            SellGoodsService sellGoods = new SellGoodsService(good)
            {
                @Override
                protected void onPostExecute(Object o)
                {
                    repo.DeleteAll();
                }
            };
            sellGoods.execute();
        }

    }

    public void SellLocalBtn(View view)
    {
        SellLocal();
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.i("MainActivity","Main Activity destroyed");
    }

}
