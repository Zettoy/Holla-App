package com.holla.group1.holla;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener,
        RestAPIClient.OnPostsLoadedListener,
        NavigationView.OnNavigationItemSelectedListener,
        OnCompleteListener<Void> {

    public static final String VIEW_POST = "view post";
    private final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private HashMap<Marker, Post> markerPostHashMap;
    private RestAPIClient apiClient;
    private Post focusedPost = null;

    public void showMakePostActivity(View view) {
        Intent intent = new Intent(MapsActivity.this, MakePostActivity.class);
        startActivity(intent);
    }
    public void showViewPostActivity(View view){
        Intent intent = new Intent(MapsActivity.this, ViewPostActivity.class);
        if(focusedPost != null){
            intent.putExtra(VIEW_POST, focusedPost.toJSON());
            startActivity(intent);
        }
    }

    public void showHistoryActivity(MenuItem item) {
        Intent intent = new Intent(MapsActivity.this, HistoryActivity.class);
        startActivity(intent);
    }

    public void showFilterActivity(MenuItem item) {
        Intent intent = new Intent(MapsActivity.this, FilterActivity.class);
        startActivity(intent);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        MapsActivityUtilities.hideOverlay(this);
        focusedPost = null;
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

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        apiClient = new RestAPIClient(getApplicationContext(), this);
        markerPostHashMap = new HashMap<>();
        MapsActivityUtilities.hideOverlay(this);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //signedInAccount = (GoogleSignInAccount) savedInstanceState.get("GoogleAccount");

        //String target = getIntent().getStringExtra("GoogleClient");
        //signedInClient = new Gson().fromJson(target, GoogleSignInClient.class);
    }

    public void openAutoCompleteActivity(View view) {
        MapSearch.openAutocompleteActivity(MapsActivity.this);
    }

    public void openNavigationDrawer(View view) {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.openDrawer(GravityCompat.START);
    }

    /**
     * Called after the autocomplete activity has finished to return its result.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.
        if (requestCode == MapSearch.REQUEST_CODE_AUTOCOMPLETE && resultCode == RESULT_OK) {
            // Get the user's selected place from the Intent.
            Place place = PlaceAutocomplete.getPlace(this, data);
            MapSearch.geoLocate((String) place.getName(), mMap, this);
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
        apiClient.loadFakeTweets();
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


    }

    private void drawPostsOnMap(List<Post> posts) {
        for (Post p : posts) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(p.getLocation());
            Marker marker = mMap.addMarker(markerOptions);
            markerPostHashMap.put(marker, p);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (markerPostHashMap.containsKey(marker)) {
            Post post = markerPostHashMap.get(marker);
            focusedPost = post;
            MapsActivityUtilities.setOverlayPost(post, this);
            MapsActivityUtilities.showOverlay(this);
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        if (menuItem.getItemId() == R.id.sign_out_itm) {
            drawerLayout.closeDrawers();
            signOut();
        }

        return true;
    }

    private void signOut() {
        if (GoogleAccountSingleton.mGoogleSignInClient != null) {
            GoogleAccountSingleton.mGoogleSignInClient.signOut().addOnCompleteListener(this, this);
        }
    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        // Google documentation didn't specify how to handle errors
        Intent startupIntent = new Intent(getBaseContext(), StartupActivity.class);
        // Prevent the user being able to press back to get back to this activity
        startupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startupIntent);
    }
}