package com.holla.group1.holla;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

@Deprecated
public class PostMarker implements GoogleMap.OnMarkerClickListener {
    private Marker marker;
    private LatLng position;

    public PostMarker(LatLng pos) {
        this.position = pos;
        //markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
    }

    public void attachToMap(GoogleMap googleMap) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(this.position);
        marker = googleMap.addMarker(markerOptions);
        googleMap.setOnMarkerClickListener(this);

    }

    public Marker getMarker() {
        return marker;
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
