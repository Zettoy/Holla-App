package com.holla.group1.holla;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMarkerClickListener, RestAPIClient.OnTweetsLoadedListener {

    private GoogleMap mMap;
    private ArrayList<PostMarker> markers;

    private EditText searchText;
    private final String TAG = "MapsActivity";

    @Override
    public void onTweetsLoaded(List<String> tweets) {
        Log.d(TAG, tweets.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        // Initialize search bar
        initSearchBar();

        mapFragment.getMapAsync(this);
        RestAPIClient apiClient = new RestAPIClient(getApplicationContext(), this);
        apiClient.loadFakeTweets();
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
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
        } else {
            int returned = 0;
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    returned);
        }

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        LatLng sydney = new LatLng(-34, 151);

        markers = new ArrayList<>();
        PostMarker postMarker = new PostMarker(sydney, "This is a missing test message. If you see this message, please return it to it's owner. #Missing", mMap);
        markers.add(postMarker);

        LatLng testLatLng = new LatLng(-33, 150);
        postMarker = new PostMarker(testLatLng, "I wish we actually connected to the backend for this stuff. #Hopeful", mMap);
        markers.add(postMarker);

        testLatLng = new LatLng(-30, 149);
        postMarker = new PostMarker(testLatLng, "I personally wish to not get grilled in the sprint review. #Wishful", mMap);
        markers.add(postMarker);

        testLatLng = new LatLng(-29, 150);
        postMarker = new PostMarker(testLatLng, "Don't forget to subscribe.", mMap);
        markers.add(postMarker);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onClick(View v) {
        /*LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.post_map_fragment, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.showAtLocation(this.findViewById(android.R.id.content)
                , Gravity.CENTER, 0, 0);*/
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //Dirty way to do it
        for (PostMarker map_marker : markers) {
            if (map_marker.getMarker().equals(marker)) {
                Fragment overlayFragment = getSupportFragmentManager().findFragmentById(R.id.post_map_overlay_frag);
                TextView overlayText = (TextView) overlayFragment.getView().findViewById(R.id.post_map_text);
                overlayText.setText(map_marker.getText());
            }
        }
        return false;
    }

    private void initSearchBar() {
        searchText = findViewById(R.id.text);

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE
                        ||  actionId == EditorInfo.IME_ACTION_SEARCH
                        ||  event.getAction() == KeyEvent.KEYCODE_ENTER
                        ||  event.getAction() == KeyEvent.ACTION_DOWN) {
                    // Search for location
                    geoLocate();
                }
                return false;
            }
        });
    }

    private void geoLocate() {
        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> addresses = new ArrayList<>();

        try {
            addresses = geocoder.getFromLocationName(
                    searchText.getText().toString(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!addresses.isEmpty()) {
            Address address = addresses.get(0);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(address.getLatitude(), address.getLongitude()), 10));
        }
    }
}