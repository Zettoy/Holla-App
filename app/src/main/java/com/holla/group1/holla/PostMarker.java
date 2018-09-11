package com.holla.group1.holla;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class PostMarker implements GoogleMap.OnMarkerClickListener {
    private Marker marker;
    private String text;

    public PostMarker(LatLng pos, String text, GoogleMap googleMap) {
        this.text = text;
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(pos);
        //markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        marker = googleMap.addMarker(markerOptions);
        googleMap.setOnMarkerClickListener(this);
    }

    public Marker getMarker() {
        return marker;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.equals(this.marker)) {
            return true;
        } else {
            return false;
        }
    }
}
