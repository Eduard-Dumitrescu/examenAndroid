package com.example.edward.tradingapp.Services;

import android.os.AsyncTask;
import android.util.Log;

import com.example.edward.tradingapp.Models.Good;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Edward on 03-Feb-17.
 */

public class BuyGoodsService extends AsyncTask
{

    private String message;
    private int statusCode;
    private Good good;


    public BuyGoodsService(Good good)
    {
        this.statusCode = 0;
        this.good = good;
    }

    public BuyGoodsService(int id, String name, int quantity, int price,int updated)
    {
        this.good = new Good(id,name,quantity,price,updated);
        this.statusCode = 0;
    }


    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Good getGood() {
        return good;
    }

    public void setGood(Good good) {
        this.good = good;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try
        {
            Log.i("BuyGoodsService","Starting to buy goods from server");

            JSONObject msg = new JSONObject();

            msg.put("name",this.good.getName());
            msg.put("quantity",this.good.getQuantity());
            msg.put("price",this.good.getPrice());

            URL url= null;

            url = new URL("http://10.0.2.2:3000/Good/buy");

            HttpURLConnection httpCon = null;

            httpCon = (HttpURLConnection) url.openConnection();

            httpCon.setRequestProperty( "Content-Type", "application/json" );
            httpCon.setRequestProperty("Accept", "application/json");
            //httpCon.setRequestProperty("Accept","*/*");

            httpCon.setRequestMethod("POST");

            httpCon.connect();


            // trimitem la server
            OutputStream os = httpCon.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");

            osw.write(msg.toString());
            osw.flush();
            osw.close();


            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader( httpCon.getInputStream(),"utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            this.message = sb.toString();

            this.statusCode=(int)httpCon.getResponseCode();

            Log.i("BuyGoodsService","Bought goods from server");

        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return  null;
    }

}
