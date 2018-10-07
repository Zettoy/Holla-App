package com.holla.group1.holla;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.holla.group1.holla.post.Post;
import com.holla.group1.holla.search.MultiSearchActivity;
import com.holla.group1.holla.signin.GoogleAccountSingleton;
import com.holla.group1.holla.signin.StartupActivity;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        OnCompleteListener<Void> {

    public static final int MAP_MOVE_LOCATION = 1;
    public static final String EXTRA_PLACE_ID = "place id";
    private final String TAG = "MapsActivity";
    protected GeoDataClient mGeoDataClient;
    private MapFragment mapFragment;

    public void showMakePostActivity(View view) {
        Intent intent = new Intent(MapsActivity.this, MakePostActivity.class);
        startActivity(intent);
    }

    public void showViewPostActivity(View view) {
        Intent intent = new Intent(MapsActivity.this, ViewPostActivity.class);
        Post focusedPost = mapFragment.getFocusedPost();
        if (focusedPost != null) {
            intent.putExtra(ViewPostActivity.BUNDLED_POST_JSON, focusedPost.toJSON());
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_linearlayout);
//        setContentView(R.layout.activity_maps);

        mapFragment = new MapFragment();

        Toolbar toolbar = findViewById(R.id.activity_maps_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayShowTitleEnabled(false);

        ViewPager viewPager = findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mGeoDataClient = Places.getGeoDataClient(this);
        //signedInAccount = (GoogleSignInAccount) savedInstanceState.get("GoogleAccount");

        //String target = getIntent().getStringExtra("GoogleClient");
        //signedInClient = new Gson().fromJson(target, GoogleSignInClient.class);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(mapFragment, "Map");
        adapter.addFragment(new FeedFragment(), "Feed");
        viewPager.setAdapter(adapter);
    }

    public void openAutoCompleteActivity(View view) {
        Intent intent = new Intent(MapsActivity.this, MultiSearchActivity.class);
        startActivityForResult(intent, MapsActivity.MAP_MOVE_LOCATION);


//        MapSearch.openAutocompleteActivity(MapsActivity.this);
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
//        if (requestCode == MapSearch.REQUEST_CODE_AUTOCOMPLETE && resultCode == RESULT_OK) {
//            // Get the user's selected place from the Intent.
//            Place place = PlaceAutocomplete.getPlace(this, data);
//            MapSearch.geoLocate((String) place.getName(), mapFragment.getmMap(), this);
//        }
        if (requestCode == MAP_MOVE_LOCATION && resultCode == Activity.RESULT_OK) {
            String place_id = data.getStringExtra(EXTRA_PLACE_ID);
            handle_map_location_change_request(place_id);
        }
    }
    private void handle_map_location_change_request(String place_id){
        mGeoDataClient.getPlaceById(place_id).addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
                if (task.isSuccessful()) {
                    PlaceBufferResponse response = task.getResult();
                    Place place = response.get(0);
                    if(mapFragment!=null){
                        mapFragment.setMapLocation(place.getLatLng());
                        //TODO: load new posts in this location
                    }
                }
            }
        });

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

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private Fragment mCurrentFragment;

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        public Fragment getCurrentFragment() {
            return mCurrentFragment;
        }

        @Override
        public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            if (getCurrentFragment() != object) {
                mCurrentFragment = ((Fragment) object);
            }
            super.setPrimaryItem(container, position, object);

        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}