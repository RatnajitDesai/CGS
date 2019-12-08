package com.darpg33.hackathon.cgs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.location.LocationManagerCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private static final String TAG = "MapsActivity";
    private static final Float DEFAULT_ZOOM = 15f;

    //widgets
    private EditText mSearchText;
    private MaterialButton mGPS, mShareLocation;
    private TextView selectedLocation, locationAccuracy;

    //vars
    private Address mAddress;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mSearchText = findViewById(R.id.etSearch);
        mGPS = findViewById(R.id.btnGPSLocation);
        mShareLocation = findViewById(R.id.shareLocation);
        selectedLocation = findViewById(R.id.txtSelectedLocation);
        locationAccuracy = findViewById(R.id.locationAccuracy);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

    }

    private void init()
    {

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
            || actionId == EditorInfo.IME_ACTION_DONE
                ||event.getAction()==KeyEvent.ACTION_DOWN
                || event.getAction() == KeyEvent.KEYCODE_ENTER)
                {

                    geoLocate();

                }

                return false;
            }
        });

        mGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: getting device location.");
                getDeviceLocation();
            }
        });

        mShareLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareLocation();
            }
        });

        hideSoftKeyBoard();

    }

    private void shareLocation() {
        Intent returnIntent = new Intent();
        Log.d(TAG, "shareLocation: share location");
        if (mAddress!=null) {
            returnIntent.putExtra("location", mAddress);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
        else {
            Snackbar.make(mShareLocation.getRootView(),"Select a location first.",Snackbar.LENGTH_SHORT).show();
        }

    }

    private void geoLocate() {

        Log.d(TAG, "geoLocate: locating.");
        String searchText = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(MapsActivity.this);

        List<Address> addressList = new ArrayList<>();

        try {
            addressList = geocoder.getFromLocationName(searchText,1);
        } catch (IOException e) {

            Log.e(TAG, "geoLocate:IOException "+e.getMessage() );
        }

        if (addressList.size() > 0 )
        {
            Address address = addressList.get(0);
            Log.d(TAG, "geoLocate: found address: "+address.toString());
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()),DEFAULT_ZOOM, address.getAddressLine(0));
            mAddress = address;
            selectedLocation.setText(mAddress.getAddressLine(0));
        }
    }


    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: get current device location.");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {

            final Task<Location> location = mFusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful())
                    {
                            Log.d(TAG, "onComplete: Device location found.");
                            Location currentLocation = task.getResult();
                            if (currentLocation != null) {
                                try {
                                    currentLocation.setAccuracy(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                                    moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),DEFAULT_ZOOM,"My Location");
                                    Geocoder geocoder = new Geocoder(MapsActivity.this);

                                    List<Address> address = geocoder.getFromLocation(currentLocation.getLatitude(),currentLocation.getLongitude(),1);
                                    mAddress = address.get(0);
                                    selectedLocation.setText(mAddress.getAddressLine(0));
                                    locationAccuracy.setText("Accuracy:"+currentLocation.getAccuracy()+" mtrs.");

                                } catch (IOException e) {
                                    e.printStackTrace();
                                    mAddress = null;
                                }
                            }
                         }
                    else {

                        Log.d(TAG, "onComplete: Device location is null.");
                        Toast.makeText(MapsActivity.this, "Unable to get device location.\nPlease enable location services from settings.", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
        catch (SecurityException e)
        {

            Log.e(TAG, "getDeviceLocation: security exception."+e.getMessage());
        }

    }


    private void moveCamera(LatLng latLng, float zoom, String title)
    {
        Log.d(TAG, "moveCamera: move camera to lat :" + latLng.latitude + ", lng:" + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
        mMap.clear();
        if (!title.equals("My Location"))
        {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(markerOptions);
        }
        hideSoftKeyBoard();
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                Geocoder geocoder = new Geocoder(MapsActivity.this);

                List<Address> addressList = new ArrayList<>();

                try {
                    addressList = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                } catch (IOException e) {

                    Log.e(TAG, "geoLocate:IOException "+e.getMessage() );
                }

                if (addressList.size() > 0 )
                {
                    Address address = addressList.get(0);
                    Log.d(TAG, "geoLocate: found address: "+address.toString());

                    moveCamera(new LatLng(address.getLatitude(), address.getLongitude()),DEFAULT_ZOOM, address.getAddressLine(0));
                    mAddress = address;
                    selectedLocation.setText(mAddress.getAddressLine(0));

                    // Setting the title for the marker.
                    // This will be displayed on taping the marker

                    markerOptions.title(mAddress.getAddressLine(0));
                }



                // Clears the previously touched position
                mMap.clear();

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);

            }
        });
        if (checkGPSEnabled()) {
            getDeviceLocation();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
        else
        {
            Toast.makeText(this, "GPS is not enabled.", Toast.LENGTH_SHORT).show();
            selectedLocation.setText(getString(R.string.gps_not_found_title));
        }
        init();
    }



    private void hideSoftKeyBoard()
    {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private boolean checkGPSEnabled(){


        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        return LocationManagerCompat.isLocationEnabled(locationManager);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged: new conf orientation :"+newConfig.orientation);
    }

}
