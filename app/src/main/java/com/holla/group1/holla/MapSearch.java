package com.holla.group1.holla;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapSearch {
    public static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private static String TAG = "MapSearch";
    public static void geoLocate(String searchText, GoogleMap mMap, Context context) {

        Geocoder geocoder = new Geocoder(context);
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
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, Config.SEARCH_RESULT_ZOOM_LEVEL));
            MarkerOptions options = new MarkerOptions()
                    .position(latLng);
            mMap.addMarker(options);

        }
    }
    public static void openAutocompleteActivity(Activity activity) {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setCountry("AU")
                    .build();
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .setFilter(typeFilter)
                    .build(activity);
            activity.startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(activity, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);
            Log.e(TAG, message);
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        }
    }
}
