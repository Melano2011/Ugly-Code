package com.example.tereshchenko.mapsshabl;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomInfoWindowAdapter(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.custom_info_window, null);

        TextView name_tv = view.findViewById(R.id.name);
        TextView details_tv = view.findViewById(R.id.details);
        TextView hotel_tv = view.findViewById(R.id.hotels);
        TextView food_tv = view.findViewById(R.id.food);
        TextView postalcode_tv = view.findViewById(R.id.postalcode);
        TextView phonenumber_tv = view.findViewById(R.id.phonenumber);

        name_tv.setText(marker.getTitle());
        details_tv.setText(marker.getSnippet());

        MapsActivity.InfoWindowData infoWindowData = (MapsActivity.InfoWindowData) marker.getTag();


        postalcode_tv.setText(infoWindowData.getPostalCode());
        hotel_tv.setText(infoWindowData.getHotel());
        food_tv.setText(infoWindowData.getFood());
        phonenumber_tv.setText(infoWindowData.getPhoneNumber());

        return view;
    }
}