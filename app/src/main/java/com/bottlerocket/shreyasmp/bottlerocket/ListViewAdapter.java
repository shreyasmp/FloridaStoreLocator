package com.bottlerocket.shreyasmp.bottlerocket;

/*
*  Creating custom list view adapter , for string arrays that will be passed  and set
* into TextViews and ImageViews for positions to be displayed in the list view
 */

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by shreyasmp on 5/3/16.
 */
public class ListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater LINflater;
    ArrayList<HashMap<String, String>> jsonData;
    HashMap<String, String> resultData = new HashMap<String, String>();
    ImageLoader imageLoader;

    public ListViewAdapter(Context context, ArrayList<HashMap<String, String>> arrayList) {
        this.context = context;
        jsonData = arrayList;
        imageLoader = new ImageLoader(context);
    }

    @Override
    public int getCount () {
        return jsonData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        // Declare Variables
        TextView address;
        TextView city;
        TextView name;
        TextView latitude;
        TextView zipcode;
        ImageView storelogo;
        TextView phone;
        TextView longitude;
        TextView storeid;
        TextView state;

        LINflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = LINflater.inflate(R.layout.listview_item, parent, false);

        resultData = jsonData.get(position);

        // Locating text views in listview xml file
        phone = (TextView) itemView.findViewById(R.id.phone);
        address = (TextView) itemView.findViewById(R.id.address);

        // locating image view in listview xml
        storelogo = (ImageView) itemView.findViewById(R.id.storelogo);

        // Set results captured from jsonData to TextViews
        phone.setText(resultData.get(MainActivity.STORE_PHONE));
        address.setText(resultData.get(MainActivity.STORE_ADDRESS));

        // Pass store logo to imageLoader class
        imageLoader.DisplayImage(resultData.get(MainActivity.STORE_STORELOGO), storelogo);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultData = jsonData.get(position);

                // Passing data each of them into MainActivity to display what is only required.
                Intent intent = new Intent(context, SingleItemView.class);

                intent.putExtra("phone", resultData.get(MainActivity.STORE_PHONE));
                intent.putExtra("address", resultData.get(MainActivity.STORE_ADDRESS));
                intent.putExtra("storeLogoURL", resultData.get(MainActivity.STORE_STORELOGO));
                intent.putExtra("city", resultData.get(MainActivity.STORE_CITY));
                intent.putExtra("name", resultData.get(MainActivity.STORE_NAME));
                intent.putExtra("latitude", resultData.get(MainActivity.STORE_LATITUDE));
                intent.putExtra("zipcode", resultData.get(MainActivity.STORE_ZIPCODE));
                intent.putExtra("longitude", resultData.get(MainActivity.STORE_LONGITUDE));
                intent.putExtra("storeID", resultData.get(MainActivity.STORE_STOREID));
                intent.putExtra("state", resultData.get(MainActivity.STORE_STATE));
                context.startActivity(intent);
            }
        });
        return itemView;
    }
}
