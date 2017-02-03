package com.example.edward.tradingapp.Services;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Edward on 03-Feb-17.
 */

public class GetGoodsService extends AsyncTask
{

    private String message;

    private int statusCode;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode)
    {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public GetGoodsService()
    {
        this.statusCode=0;
    }

    @Override
    protected Object doInBackground(Object[] params)
    {
        try
        {
            Log.i("GetGoodsService","Starting to get goods from server");

            URL url= null;

            url = new URL("http://10.0.2.2:3000/Good?lastUpdated=100");

            HttpURLConnection httpCon = null;

            httpCon = (HttpURLConnection) url.openConnection();




            httpCon.setRequestProperty( "Content-Type", "application/json" );
            httpCon.setRequestProperty("Accept", "application/json");

            httpCon.setRequestMethod("GET");

            httpCon.connect();

            this.statusCode=httpCon.getResponseCode();

            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader( httpCon.getInputStream(),"utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();

            this.message = sb.toString();

            this.statusCode=(int)httpCon.getResponseCode();

            Log.i("GetGoodsService","Received Goods From Server");

        }
        catch (IOException e) {
            e.printStackTrace();
        }




        return  null;
    }
}
