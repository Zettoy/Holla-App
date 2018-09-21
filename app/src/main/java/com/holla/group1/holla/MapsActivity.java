package com.holla.group1.holla;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        View.OnClickListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener,
        RestAPIClient.OnPostsLoadedListener {

    private final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private HashMap<Marker, Post> markerPostHashMap;
    private String searchText;
    private RestAPIClient apiClient;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;


    public void showMakePostActivity(View view){
        Intent intent = new Intent(MapsActivity.this, MakePostActivity.class);
        startActivity(intent);
    }
    private void showOverlay() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        Fragment overlayFragment = getSupportFragmentManager().findFragmentById(R.id.post_map_overlay_frag);
        ft.show(overlayFragment);
        ft.commit();
    }

    private void hideOverlay() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        Fragment overlayFragment = getSupportFragmentManager().findFragmentById(R.id.post_map_overlay_frag);
        ft.hide(overlayFragment);
        ft.commit();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        hideOverlay();
    }

    @Override
    public void onPostsLoaded(List<Post> posts) {
        drawPostsOnMap(posts);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_linearlayout);
//        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        // Open the autocomplete activity when the button is clicked.
        Button openButton = findViewById(R.id.open_button);
        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAutocompleteActivity();
            }
        });
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        apiClient = new RestAPIClient(getApplicationContext(), this);
        markerPostHashMap = new HashMap<>();
        hideOverlay();
    }

    private void openAutocompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setCountry("AU")
                    .build();
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .setFilter(typeFilter)
                    .build(this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);
            Log.e(TAG, message);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Called after the autocomplete activity has finished to return its result.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place Selected: " + place.getName());
                searchText = (String)place.getName();
                geoLocate();
            }
        }
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
        apiClient.loadFakeTweets(this.getApplicationContext());
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

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Config.STARTING_LOCATION, Config.STARTING_ZOOM_LEVEL));
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);

        addSamplePosts();

    }
    private void drawPostsOnMap(List<Post> posts){
        for (Post p : posts) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(p.getLocation());
            Marker marker = mMap.addMarker(markerOptions);
            markerPostHashMap.put(marker, p);
        }
    }
    private void addSamplePosts() {
        drawPostsOnMap(SamplePosts.getSamplePosts());
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

    public void setOverlayPost(Post post) {
        PostMapOverlay overlayFragment = (PostMapOverlay) getSupportFragmentManager().findFragmentById(R.id.post_map_overlay_frag);
        overlayFragment.showPost(post);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Post post = markerPostHashMap.get(marker);
        setOverlayPost(post);
        showOverlay();
        return false;
    }

    private void geoLocate() {
        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> addresses = new ArrayList<>();

        try {
            addresses = geocoder.getFromLocationName(
                    searchText, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!addresses.isEmpty()) {
            Address address = addresses.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
            MarkerOptions options = new MarkerOptions()
                    .position(latLng);
            mMap.addMarker(options);

        }
    }
}