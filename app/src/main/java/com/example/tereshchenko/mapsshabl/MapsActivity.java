package com.example.tereshchenko.mapsshabl;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.tereshchenko.mapsshabl.models.PlaceInfo;

import com.example.tereshchenko.mapsshabl.models.PlaceInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener{
    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
    private GoogleMap mMap;
    Marker q;

    //Варианты для разрешения на использования геолокации
    private Boolean my_locationPermissonsGranted = false;

    //Для местоположения
    private FusedLocationProviderClient mFusedLocationProviderClient;

    //Для автозаполнения вариантов
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private PlaceInfo mPlace;


    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -168), new LatLng(71,136));

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    public class InfoWindowData {
        private String PostalCode;
        private String hotel;
        private String food;
        private String PhoneNumber;


        public String getHotel() {
            return hotel;
        }

        public void setHotel(String hotel) {
            this.hotel = hotel;
        }

        public String getFood() {
            return food;
        }

        public void setFood(String food) {
            this.food = food;
        }
        public String getPostalCode() {
            return PostalCode;
        }

        public void setPostalCode(String PostalCode) {
            this.PostalCode = PostalCode;
        }

        public String getPhoneNumber() {
            return PhoneNumber;
        }

        public void setPhoneNumber(String PhoneNumber) {
            this.PhoneNumber = PhoneNumber;
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Карты готовы!", Toast.LENGTH_SHORT).show();

        //Отслеживание по терминалу::::::::
        Log.d("onMapReady", "onMapReady: MAP IS READY __________________");

        mMap = googleMap;
        //Marker q;
        // Add a marker in Sydney and move the camera
        //LatLng rostov = new LatLng(47.258016, 39.651040);
        //Создадим маркер!
        //mMap.addMarker(new MarkerOptions().position(rostov).title("Marker in Rostov!"));

        //Аля по нажатиюпоказывается текст маркера
        /*q = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Hello world")
                .snippet("Additional text"));*/

        //Приблизим камеру!
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(rostov,15F));

        if (my_locationPermissonsGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);

        }

            init();
        }


    //widgets
    private AutoCompleteTextView mSearchText;
    private ImageView mGps, mInfo   ;
    //vars
    private GoogleApiClient getmGoogleApiClient;
    private PlaceAutocompleteFragment placeAutocompleteFragment;
    private Marker mMarker;

    Marker marker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mSearchText = (AutoCompleteTextView) findViewById(R.id.input_search);
        mGps = (ImageView) findViewById(R.id.ic_gps);
        mInfo = (ImageView) findViewById(R.id.place_info);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        getLocalPermission();
        //init();

    }

    private void init()
    {
        Log.d("init","init: initializing");

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this,this)
                .build();
        mSearchText.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient,
                LAT_LNG_BOUNDS, null);
        mSearchText.setAdapter(mPlaceAutocompleteAdapter);
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER)
                {

                    //вызов метода для поиска
                    geoLocate();
                }
                return false;
            }
        });
        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onClick","onClick: clicked gps icon");
                getDeviceLocation();
            }
        });

        mInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onClick", "onClick: clicked place info");
                try{
                    if(mMarker.isInfoWindowShown()){
                        mMarker.hideInfoWindow();
                    }else{
                        Log.d("onClick","onClick: place info: "+ mPlace.toString());
                        mMarker.showInfoWindow();
                    }
                }catch (NullPointerException e){
                    Log.e("onClick","onClick: NullPointerException: "+ e.getMessage());
                }
            }
        });
        hideSoftKeyboard();
    }

    private void geoLocate()
    {
        Log.d("geolocate","geolocate:geolocating");

        String searchString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> list = new ArrayList<>();
        try
        {
            list = geocoder.getFromLocationName(searchString,1);
        }catch (IOException e){
            Log.e("geoLocate","geolocate: IOException " + e.getMessage());
        }

        if (list.size() > 0)
        {
            Address address = list.get(0);

            Log.d("geolocate","geoLocate: found a location: " + address.toString());
            //Toast.makeText(this,address.toString(),Toast.LENGTH_SHORT).show();

            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),15f, address.getAddressLine(0));
        }
    }

    //Отслеживаем местоположение усройства
    private void getDeviceLocation()
    {
        Log.d("getDeviceLocation","getDeviceLocation: местоположение отслеживается!");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try
        {
            if (my_locationPermissonsGranted)
            {
                Task<Location> location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful() && task.getResult() != null)
                        {
                            Log.d("addOnCompleteListener","addOnCompleteListener: локация найдена!");
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),15f,"Ваше местоположение!");
                        }
                        else
                        {
                            Log.d("addOnCompleteListener","addOnCompleteListener: локация не найдена!");
                            Toast.makeText(MapsActivity.this,"Невозможно определить ваше местоположение", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e)
        {
            Log.e("getDeviceLocation","getDeviceLocation: " + e.getMessage());
        }
    }


    private void moveCamera(LatLng latLng, float zoom, PlaceInfo placeInfo){
        Log.d("", "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        mMap.clear();
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this ));
        if(placeInfo != null){
            try{
                String snippet = "Address" ;
                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .title(placeInfo.getName())
                        .snippet(snippet);
                mMarker = mMap.addMarker(options);
                mMap.addMarker(options);
            }catch (NullPointerException e){
                Log.e("", "moveCamera: NullPointerException: " + e.getMessage());
            }
        }else{
            mMap.addMarker(new MarkerOptions().position(latLng));
        }

        hideSoftKeyboard();
    }
    //Information
    public String getAddress(double LATITUDE, double LONGITUDE) {

        String address = "";
        String postalCode = "";
        //Set Address
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null && addresses.size() > 0) {



                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL



            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return   address;
    } public String getPostalCode(double LATITUDE, double LONGITUDE) {

        String PostalCode = "";

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null && addresses.size() > 0) {
                PostalCode = addresses.get(0).getPostalCode();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return   PostalCode;
    }
    public String getPhoneNumber(double LATITUDE, double LONGITUDE) {

        String PhoneNumber = "";

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null && addresses.size() > 0) {
                PhoneNumber = addresses.get(0).getPhone();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(PhoneNumber != null)
        return   PhoneNumber;
        else
            return "NO Phone Number For This Place";
    }
    //Пишем свою камеру
    private void moveCamera(LatLng latLng,float zoom, String title)
    {
        Log.d("moveCamera","moveCamera: двигаем камеру на: lat: " + latLng.latitude + " ,lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));

       String snippet = getAddress(latLng.latitude,latLng.longitude);

        if (!title.equals("Ваше местоположение!"))
        {

            MarkerOptions options = new MarkerOptions().position(latLng).title(title).snippet(snippet);
            InfoWindowData info = new InfoWindowData();
            /*info.setHotel("Hotel : excellent hotels available");
            info.setFood("Food : all types of restaurants available");*/
            info.setPostalCode("PostalCode : " + getPostalCode(latLng.latitude,latLng.longitude));
            info.setPhoneNumber("Phone Number : " + getPhoneNumber(latLng.latitude,latLng.longitude));
            CustomInfoWindowAdapter customInfoWindow = new CustomInfoWindowAdapter(this);
            mMap.setInfoWindowAdapter(customInfoWindow);

            Marker m = mMap.addMarker(options);
            m.setTag(info);
            m.showInfoWindow();

            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));


        }
        hideSoftKeyboard();
    }

    //Инициализация карты
    private void InitMap()
    {
        Log.d("InitMap","InitMap: MAP IS init ___");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapsActivity.this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    //Проверка на наличие необходимых разрешений на использование геолокации
    private void getLocalPermission()
    {
        Log.d("getLocalPermission","getLocalPermission: разрешение получено! ___");

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                my_locationPermissonsGranted = true;
                InitMap();
            }
            else
            {
                ActivityCompat.requestPermissions(this,permissions,1234);
            }
        }
        else
        {
            ActivityCompat.requestPermissions(this,permissions,1234);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        my_locationPermissonsGranted = false;
        switch (requestCode)
        {
            case 1234:
            {
                if(grantResults.length > 0)
                {
                    for (int i = 0; i < grantResults.length; i++)
                    {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                        {
                            my_locationPermissonsGranted=false;
                            Log.d("onRequestPerm","onRequestPermissionsResult: разрешение не получено!!!!! ___");
                            return;
                        }
                    }
                    my_locationPermissonsGranted = true;
                    Log.d("onRequestPerm","onRequestPermissionsResult: разрешение получено!!!!!!! ___");
                    //инициализируем карты
                    InitMap();
                }
            }
        }

    }

    private void hideSoftKeyboard()
    {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /*
    ------------- google places API autocomplete suggestions
     */

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            hideSoftKeyboard();

            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(i);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if(!places.getStatus().isSuccess()){
                Log.d("onResult","onresult: Place query didnt compl succ: " + places.getStatus().toString());
                places.release();
                return;
            }
            final Place place = places.get(0);
            try{
                mPlace = new PlaceInfo();
                mPlace.setName(place.getName().toString());
                Log.d("", "onResult: name: " + place.getName());
                mPlace.setAddress(place.getAddress().toString());
                Log.d("", "onResult: address: " + place.getAddress());
            //    mPlace.setAttributions(place.getAttributions().toString());
            //    Log.d("", "onResult: attributions: " + place.getAttributions());
                mPlace.setId(place.getId());
                Log.d("", "onResult: id:" + place.getId());
                mPlace.setLatlng(place.getLatLng());
                Log.d("", "onResult: latlng: " + place.getLatLng());
                mPlace.setRating(place.getRating());
                Log.d("", "onResult: rating: " + place.getRating());
                mPlace.setPhoneNumber(place.getPhoneNumber().toString());
                Log.d("", "onResult: phone number: " + place.getPhoneNumber());
                mPlace.setWebsiteUri(place.getWebsiteUri());
                Log.d("", "onResult: website uri: " + place.getWebsiteUri());
                Log.d("", "onResult: place: " + mPlace.toString());
            }catch (NullPointerException e){
                Log.e("", "onResult: NullPointerException: " + e.getMessage() );
            }
            moveCamera(new LatLng(place.getViewport().getCenter().latitude,
                    place.getViewport().getCenter().longitude), DEFAULT_KEYS_DIALER, mPlace.getName());



            places.release();
        }
    };
}
