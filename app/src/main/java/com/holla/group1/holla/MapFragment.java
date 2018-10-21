package com.holla.group1.holla;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
    public void onResume() {
        super.onResume();
        if (mMap != null) {
            refreshPosts();
            focusedPost = null;
            MapFragmentUtilities.hideOverlay(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, parent, false);

        apiClient = new RestAPIClient(getContext(), this, null);
        markerPostHashMap = new HashMap<>();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);

        MapFragmentUtilities.hideOverlay(this);

        return view;
    }

    private final static int REQUEST_CODE_LOCATION = 1;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(getContext(), "Location permission granted", Toast.LENGTH_LONG).show();
            setupMap(mMap);
        } else {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_CODE_LOCATION : {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(this.mMap!=null) {
                        setupMap(this.mMap);
                    }
                }
            }
        }
    }

    private void setupMap(GoogleMap map) {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        setMapLocationAndLoadPosts(Config.STARTING_LOCATION);
        map.setOnMarkerClickListener(this);
        map.setOnMapClickListener(this);
        refreshPosts();
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
        //apiClient.createComment();
        //apiClient.getCommentsFromPostID("5bba12f6053a101f009c7c11");
    }


    public void setMapLocationAndLoadPosts(LatLng location){
        if(mMap != null){

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, Config.SEARCH_RESULT_ZOOM_LEVEL));
            //TODO: load new posts in this location
            apiClient.getPostsAtLocation(location);
        }
    }
    private void drawPostsOnMap(List<Post> posts) {
        mMap.clear();
        markerPostHashMap.clear();

        for (Post p : posts) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(p.getLocation());
            Marker marker = mMap.addMarker(markerOptions);
            markerPostHashMap.put(marker, p);
        }
    }
    public void refreshPosts() {
        if(mMap!=null){
            LatLng loc = mMap.getCameraPosition().target;
            setMapLocationAndLoadPosts(loc);
        }
    }

    public GoogleMap getmMap() {
        return mMap;
    }

    public Post getFocusedPost() {
        return focusedPost;
    }
}
