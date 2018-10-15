package com.holla.group1.holla.search.location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class LocationSearchResult {





    public static class Item {
        public final String location_name;
        //https://developers.google.com/places/android-sdk/place-id
        //https://developers.google.com/places/android-sdk/place-details
        public final String place_id;

        public Item(String place_id, String location_name) {
            this.location_name = location_name;
            this.place_id = place_id;
        }

        @Override
        public String toString() {
            return this.location_name;
        }
    }
}
