package com.example.edward.tradingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.edward.tradingapp.Models.Good;
import com.example.edward.tradingapp.Services.BuyGoodsService;
import com.example.edward.tradingapp.Services.SellGoodsService;

public class DetailGoodsActivity extends AppCompatActivity
{
    private EditText goodName;
    private EditText goodPrice;
    private EditText goodQuantity;

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

        if(isBuy)
        {
            currentGood = new Good(getIntent().getIntExtra("Id",0),getIntent().getStringExtra("Name"),getIntent().getIntExtra("Quantity",0),getIntent().getIntExtra("Price",0),getIntent().getIntExtra("Updated",0));
            goodName.setText(currentGood.getName());
            goodPrice.setText(currentGood.getPrice() + "");
            goodQuantity.setText(currentGood.getQuantity()+ "");
        }


        goodName.setEnabled(!isBuy);

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
                    Intent intent = new Intent(DetailGoodsActivity.this,MainActivity.class);

                    startActivity(intent);
                }
            };

            buyGoods.execute();

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

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.i("DetailGoodsActivity","Detail Goods Activity destroyed");
    }

}
