package com.holla.group1.holla;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.holla.group1.holla.api.RequestQueueSingleton;
import com.holla.group1.holla.api.RestAPIClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class MakePostActivity extends AppCompatActivity {

    private static final String TAG = "MakePostActivity";

    private FusedLocationProviderClient mFusedLocationClient;
    private Location location;
    private TextView locationText;
    private EditText post;

    private RestAPIClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_post);

        Toolbar toolbar = findViewById(R.id.activity_make_post_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationText = findViewById(R.id.location_text);
        post = findViewById(R.id.post_edit_text);

        //TODO: add button delay maybe?
        findViewById(R.id.send_post_btn)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sendPost()) finish();
                    }
                });

        findViewById(R.id.locate_btn)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });

        // Automatically pop up keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        apiClient = new RestAPIClient(this, null, null);
    }

    @Override
    protected void onStart() {
        super.onStart();

        getLocation();
    }

    private boolean sendPost() {
        //TODO: add more condition checking
        if (post.getText().length() > 0) {
            //postRequest();
            apiClient.createPost(new LatLng(location.getLatitude(), location.getLongitude()), "testboy", post.getText().toString());
            Toast.makeText(this, "Post has been sent.", Toast.LENGTH_LONG).show();
            return true;
        } else {
            Toast.makeText(this, "Empty Post", Toast.LENGTH_LONG).show();
        }

        return false;
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
            Log.e(TAG, "setLocationText: " + e.getMessage());

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
