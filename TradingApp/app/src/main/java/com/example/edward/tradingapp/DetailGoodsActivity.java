package com.example.edward.tradingapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.edward.tradingapp.Models.Good;
import com.example.edward.tradingapp.Repositories.GoodsSPRepository;
import com.example.edward.tradingapp.Repositories.LocalDBHandler;
import com.example.edward.tradingapp.Services.BuyGoodsService;
import com.example.edward.tradingapp.Services.SellGoodsService;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailGoodsActivity extends AppCompatActivity
{
    private EditText goodName;
    private EditText goodPrice;
    private EditText goodQuantity;
    private Button btnSubmit;

    private Good currentGood;
    private boolean isBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_goods);

        Log.i("DetailGoodsActivity","Detail Goods Activity created");

        isBuy = getIntent().getBooleanExtra("IsBuy",true);

        goodName = (EditText) findViewById(R.id.goodNameInput);
        goodPrice = (EditText) findViewById(R.id.goodPriceInput);
        goodQuantity = (EditText) findViewById(R.id.goodQunatityInput);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        if(getIntent().getBooleanExtra("Transaction",true))
        {
            currentGood = new Good(getIntent().getIntExtra("Id",0),getIntent().getStringExtra("Name"),getIntent().getIntExtra("Quantity",0),getIntent().getIntExtra("Price",0),getIntent().getIntExtra("Updated",0));
            goodName.setText(currentGood.getName());
            goodPrice.setText(currentGood.getPrice() + "");
            goodQuantity.setText(currentGood.getQuantity()+ "");

            goodName.setEnabled(false);
            goodPrice.setEnabled(false);
            goodQuantity.setEnabled(false);
            btnSubmit.setEnabled(false);

        } else
        {
            if(isBuy)
            {
                currentGood = new Good(getIntent().getIntExtra("Id", 0), getIntent().getStringExtra("Name"), getIntent().getIntExtra("Quantity", 0), getIntent().getIntExtra("Price", 0), getIntent().getIntExtra("Updated", 0));
                goodName.setText(currentGood.getName());
                goodPrice.setText(currentGood.getPrice() + "");
                goodQuantity.setText(currentGood.getQuantity() + "");
            }
            goodName.setEnabled(!isBuy);
        }




    }

    public void Submit(View view)
    {




        if(this.isBuy)
        {
            Good goodToSend = new Good(currentGood.getId(),goodName.getText().toString(),Integer.parseInt(goodQuantity.getText().toString()),Integer.parseInt(goodPrice.getText().toString()),currentGood.getUpdated());
            BuyGoodsService buyGoods = new BuyGoodsService(goodToSend)
            {
                @Override
                protected void onPostExecute(Object o)
                {
                    if(this.getStatusCode() == 200)
                    {
                        GoodsSPRepository repo = new GoodsSPRepository(DetailGoodsActivity.this);

                        try {
                            JSONObject obj = new JSONObject(this.getMessage());


                            repo.AddGood(new Good(obj.getInt("id"),obj.getString("name"),obj.getInt("quantity"),obj.getInt("price"),0));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }

                    Intent intent = new Intent(DetailGoodsActivity.this,MainActivity.class);

                    startActivity(intent);
                }
            };

            buyGoods.execute();

        }
        else
        {
            if(!isNetworkAvailable())
            {
                LocalDBHandler repo = new LocalDBHandler(this,null,null,1);

                repo.AddGood(new Good(0,goodName.getText().toString(),Integer.parseInt(goodQuantity.getText().toString()),Integer.parseInt(goodPrice.getText().toString()),200));

                Intent intent = new Intent(DetailGoodsActivity.this,MainActivity.class);

                startActivity(intent);

            }
            else
            {
                SellGoodsService sellGoods = new SellGoodsService(0,goodName.getText().toString(),Integer.parseInt(goodQuantity.getText().toString()),Integer.parseInt(goodPrice.getText().toString()),0)
                {
                    @Override
                    protected void onPostExecute(Object o)
                    {
                        Intent intent = new Intent(DetailGoodsActivity.this,MainActivity.class);

                        startActivity(intent);
                    }
                };
                sellGoods.execute();
            }

        }
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.i("DetailGoodsActivity","Detail Goods Activity destroyed");
    }

}
