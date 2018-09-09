package com.holla.group1.holla;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class PostMarker implements GoogleMap.OnMarkerClickListener {
    private Marker marker;
    private String text;
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint fontPaint;
    private boolean testBool = false;

    public PostMarker(LatLng pos, String text, GoogleMap googleMap) {
        this.text = text;

        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        bitmap = Bitmap.createBitmap(300, 60, conf);
        canvas = new Canvas(bitmap);

        fontPaint = new Paint();
        fontPaint.setTextSize(35);
        fontPaint.setColor(Color.BLACK);
        canvas.drawColor(Color.WHITE);
        canvas.drawText(text, 30, 40, fontPaint);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(pos);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        marker = googleMap.addMarker(markerOptions);
        googleMap.setOnMarkerClickListener(this);
    }

    public Marker getMarker() {
        return marker;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d("Holla", "I'm here");
        if (marker.equals(this.marker)) {
            if (testBool) {
                canvas.drawColor(Color.WHITE);
            } else {
                canvas.drawColor(Color.BLACK);
            }
            testBool = !testBool;

            canvas.drawText(text, 30, 40, fontPaint);
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
            Log.d("Holla", "Me too");
            return true;
        } else {
            return false;
        }
    }
}
