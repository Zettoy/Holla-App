package com.holla.group1.holla;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.holla.group1.holla.api.RestAPIClient;
import com.holla.group1.holla.post.Post;
import com.holla.group1.holla.util.MapFragmentUtilities;

import java.util.HashMap;
import java.util.List;

public class MapFragment extends Fragment implements
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener,
        RestAPIClient.OnPostsLoadedListener {

    private GoogleMap mMap;
    private HashMap<Marker, Post> markerPostHashMap;
    private RestAPIClient apiClient;
    private Post focusedPost = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, parent, false);

        apiClient = new RestAPIClient(getContext(), this);
        markerPostHashMap = new HashMap<>();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);

        MapFragmentUtilities.hideOverlay(this);

        return view;
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

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "Permission granted", Toast.LENGTH_LONG).show();
        } else {
            int returned = 0;
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    returned);
        }

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Config.STARTING_LOCATION, Config.STARTING_ZOOM_LEVEL));
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        MapFragmentUtilities.hideOverlay(this);
        focusedPost = null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (markerPostHashMap.containsKey(marker)) {
            Post post = markerPostHashMap.get(marker);
            focusedPost = post;
            MapFragmentUtilities.setOverlayPost(post, this);
            MapFragmentUtilities.showOverlay(this);
        }
        return false;
    }

    @Override
    public void onPostsLoaded(List<Post> posts) {
        drawPostsOnMap(posts);
    }

    public void setMapLocation(LatLng location){
        if(mMap != null){

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, Config.SEARCH_RESULT_ZOOM_LEVEL));
            //TODO: load new posts in this location
            apiClient.getPostsAtLocation(location, 1000);
        }
    }
    private void drawPostsOnMap(List<Post> posts) {
        for (Post p : posts) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(p.getLocation());
            Marker marker = mMap.addMarker(markerOptions);
            markerPostHashMap.put(marker, p);
        }
    }

    public GoogleMap getmMap() {
        return mMap;
    }

    public Post getFocusedPost() {
        return focusedPost;
    }
}
