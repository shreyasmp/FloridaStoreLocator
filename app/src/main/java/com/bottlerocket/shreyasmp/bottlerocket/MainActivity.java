package com.bottlerocket.shreyasmp.bottlerocket;

/*
* Main Activity of App which is critical in handling the json data and
* parsing the json and sending to List Adapter for list view display
*
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity {

    // Declaring Variables

    JSONObject jsonObject;
    JSONArray jsonArray;
    ListView listview;
    ListViewAdapter adapter;

    ProgressDialog progressDialog;
    ArrayList<HashMap<String, String>> arrayList;

    static String STORE_ADDRESS = "address";
    static String STORE_CITY = "city";
    static String STORE_NAME = "name";
    static String STORE_LATITUDE = "latitude";
    static String STORE_ZIPCODE = "zipcode";
    static String STORE_STORELOGO = "storeLogoURL";
    static String STORE_PHONE = "phone";
    static String STORE_LONGITUDE = "longitude";
    static String STORE_STOREID = "storeID";
    static String STORE_STATE = "state";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get list view from activity_main.xml
        setContentView(R.layout.activity_main);

        // execute downloadJSON AsyncTask to enable use of UI Threads for performing
        // background actions and publishing results to UI
        new DownloadJSON().execute();
    }

    // Class implementing AsyncTask operations
    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        // Creating progress dialog to show user what is happening
        // along with a title and message while json data is fetched from server
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Bottle Rocket JSON");
            progressDialog.setMessage("Downloading.....");
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        // Function to perform background tasks like fetching json data and storing them in a HashMap
        @Override
        protected Void doInBackground(Void... params) {
            // Create an arrayList of HashMaps
            arrayList = new ArrayList<HashMap<String, String>>();

            //Retrieve json objects from provided URL
            jsonObject = JSONfunctions.getJSONfromURL("http://sandbox.bottlerocketapps.com/BR_Android_CodingExam_2015_Server/stores.json");

            try {
                // Identify the json array name from url
                jsonArray = jsonObject.getJSONArray("stores");
                for (int objectIndex = 0; objectIndex < jsonArray.length(); objectIndex++) {
                    HashMap<String, String> hmap = new HashMap<String, String>();
                    jsonObject = jsonArray.getJSONObject(objectIndex);

                    // Retrieve the json objects and store them in hashmap
                    hmap.put("storeLogoURL", jsonObject.getString("storeLogoURL"));
                    hmap.put("phone", jsonObject.getString("phone"));
                    hmap.put("address", jsonObject.getString("address"));
                    hmap.put("city", jsonObject.getString("city"));
                    hmap.put("name", jsonObject.getString("name"));
                    hmap.put("latitude", jsonObject.getString("latitude"));
                    hmap.put("zipcode", jsonObject.getString("zipcode"));
                    hmap.put("longitude", jsonObject.getString("longitude"));
                    hmap.put("storeID", jsonObject.getString("storeID"));
                    hmap.put("state", jsonObject.getString("state"));

                    // set json objects into array
                    arrayList.add(hmap);
                }
            } catch (JSONException e) {
                Log.e("Error Parsing JSON: ", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        // Locating the listview in listview.xml
        // Passing results into ListAdapter custom class
        // closing the progress dialog once data is retrieved
        @Override
        protected void onPostExecute(Void args) {
            listview = (ListView) findViewById(R.id.listview);
            adapter = new ListViewAdapter(MainActivity.this, arrayList);
            listview.setAdapter(adapter);
            progressDialog.dismiss();
        }
    }
}
