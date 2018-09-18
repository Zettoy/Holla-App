package com.holla.group1.holla;


import android.Manifest;
import android.content.pm.PackageManager;

import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MakePostActivity extends AppCompatActivity {

    private FusedLocationProviderClient mFusedLocationClient;
    private Location location;
    private TextView locationText;

    private EditText post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_post);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        findViewById(R.id.make_post_close_btn)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

        findViewById(R.id.send_post_btn)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO: send post
                    }
                });

        findViewById(R.id.locate_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });

        locationText = findViewById(R.id.location_text);

        post = findViewById(R.id.post_edit_text);

        // Automatically pop up keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

    }

    @Override
    protected void onStart() {
        super.onStart();

        getLocation();

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Task t = mFusedLocationClient.getLastLocation();
        t.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    location = (Location) task.getResult();
                    setLocationText();
                }
            }
        });
    }

    private void setLocationText() {
        Geocoder geocoder = new Geocoder(MakePostActivity.this);

        double lat = location.getLatitude();
        double lng = location.getLongitude();

        try {
            String address = geocoder
                    .getFromLocation(lat, lng, 1)
                    .get(0)
                    .getAddressLine(0);

            // TODO: find a better way to limit the length of address
            address = address.split(",")[0];

            locationText.setText(address);

        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
