package com.bottlerocket.shreyasmp.bottlerocket;

/*
* JSON Functions class main operation is to create a http client connection
* to download json objects and perform necessary string conversions to stanadard
* ASCII encoding.
 */

import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by shreyasmp on 5/3/16.
 */
public class JSONfunctions {

    public static JSONObject getJSONfromURL(String url) {

        InputStream inputStream = null;
        String result = "";
        JSONObject jObj = null;

        // Downloading JSON data from URL provided

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            inputStream = entity.getContent();

        } catch (Exception e) {
            Log.e("Logging", "Http Connection Error: " +e.toString());
        }

        // Converting response from json into string, standars ASCII encoding conversion
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
            StringBuilder sBuilder = new StringBuilder();
            String lineRead = null;
            while((lineRead = reader.readLine()) != null) {
                sBuilder.append(lineRead + "\n");
            }
            inputStream.close();
            result = sBuilder.toString();
        } catch (Exception e) {
            Log.e("Logging", "Parsing Error " + e.toString());
        }

        try {
            jObj = new JSONObject(result);
        } catch (JSONException e) {
            Log.e("Logging", "Parsing Error " + e.toString());
        }

        return jObj;
    }
}
