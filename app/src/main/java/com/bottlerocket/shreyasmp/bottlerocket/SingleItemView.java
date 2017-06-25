package com.bottlerocket.shreyasmp.bottlerocket;

/*
* SingleItem View class used for displaying the details of selected Store into next activity screen
* using intents the objects are pushed to activity class with help of single item xml file with ordering
* defined. The coordinates in detail page are used to be displayed as coordinates on the google maps
*
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by shreyasmp on 5/4/16.
 */
public class SingleItemView extends FragmentActivity {

    // Declaring variables

    String address;
    String city;
    String name;
    String latitude;
    String zipcode;
    String storelogo;
    String phone;
    String longitude;
    String storeid;
    String state;

    ImageLoader imageLoader = new ImageLoader(this);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // getting view from single item xml
        setContentView(R.layout.singleitemview);

        Intent i = getIntent();

        // Getting results of each json array objects
        storelogo = i.getStringExtra("storeLogoURL");
        name = i.getStringExtra("name");
        address = i.getStringExtra("address");
        city = i.getStringExtra("city");
        state = i.getStringExtra("state");
        zipcode = i.getStringExtra("zipcode");
        phone = i.getStringExtra("phone");
        latitude = i.getStringExtra("latitude");
        longitude = i.getStringExtra("longitude");
        storeid = i.getStringExtra("storeID");

        // Locating imageview and text view from singleitemview.xml

        ImageView store_Logo = (ImageView) findViewById(R.id.storelogo);
        TextView store_name = (TextView) findViewById(R.id.name);
        TextView store_address = (TextView) findViewById(R.id.address);
        TextView store_city = (TextView)findViewById(R.id.city);
        TextView store_state = (TextView) findViewById(R.id.state);
        TextView store_zipcode = (TextView) findViewById(R.id.zipcode);
        TextView store_phone = (TextView) findViewById(R.id.phone);
        TextView store_storeid = (TextView) findViewById(R.id.storeid);


        // Converting String to Double for latitude and longitude

        double storeLat = Double.parseDouble(latitude);
        double storeLon = Double.parseDouble(longitude);

        // using latitude and longitude function for creating a position for store
        LatLng position = new LatLng(storeLat, storeLon);

        // instantiating marker options class
        // setting position , title and coordinates snippet for marker on map
        MarkerOptions options = new MarkerOptions();
        options.position(position);
        options.title("Coordinates: ");
        options.snippet("Lat: "+storeLat+" Lon: "+storeLon);

        // getting reference to support map fragment of main xml file
        SupportMapFragment sfm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);

        // reference to google map and adding marker to the map
        GoogleMap googleMap = sfm.getMap();
        googleMap.addMarker(options);

        // creating camera update object for position
        // creating camera position object for zoom
        CameraUpdate storePosition = CameraUpdateFactory.newLatLngZoom(position, 11.0f);
        googleMap.animateCamera(storePosition);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position)   // Setting center
                .zoom(11.0f)        // zoom
                .bearing(90)        // orientation of camera set roughly to 90 degree
                .tilt(30)           // tilt camera to 30 degrees
                .build();           // creating camera position from builder

        // applying zoom to marker position
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        // Setting results to text views for displaying
        // pass logo images url into imageloader class
        imageLoader.DisplayImage(storelogo, store_Logo);
        store_name.setText(name);
        store_address.setText(address);
        store_city.setText(city);
        store_state.setText(state);
        store_zipcode.setText(zipcode);
        store_phone.setText(phone);
        store_storeid.setText(storeid);

    }
}
