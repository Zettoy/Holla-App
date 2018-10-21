package com.holla.group1.holla;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
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
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.holla.group1.holla.api.RestAPIClient;
import com.holla.group1.holla.post.Post;
import com.holla.group1.holla.search.MultiSearchActivity;
import com.holla.group1.holla.signin.GoogleAccountSingleton;
import com.holla.group1.holla.signin.StartupActivity;
import com.holla.group1.holla.user.User;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        OnCompleteListener<Void> {

    public static final int MAP_MOVE_LOCATION = 1;
    public static final String EXTRA_PLACE_ID = "place id";
    private final String TAG = "MapsActivity";
    private final int CREATE_POST_ACTIVITY = 1284;

    protected GeoDataClient mGeoDataClient;
    private MapFragment mapFragment;

    private RestAPIClient apiClient;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(mapFragment!=null){
            mapFragment.onRequestPermissionsResult(requestCode,permissions,grantResults);
        }
    }

    public void showMakePostActivity(View view) {
        Intent intent = new Intent(MapsActivity.this, MakePostActivity.class);
        startActivityForResult(intent, CREATE_POST_ACTIVITY);
    }

    public void showViewPostActivity(View view) {
        Intent intent = new Intent(MapsActivity.this, ViewPostActivity.class);
        Post focusedPost = mapFragment.getFocusedPost();
        if (focusedPost != null) {
            intent.putExtra(ViewPostActivity.BUNDLED_POST_ID, focusedPost.getId());
            startActivity(intent);
        }
    }

    public void showProfileActivity(MenuItem item) {
        Intent intent = new Intent(MapsActivity.this, ProfileActivity.class);
        String name = "";
        if(GoogleAccountSingleton.mGoogleSignInAccount!=null){
            GoogleSignInAccount account = GoogleAccountSingleton.mGoogleSignInAccount;
            name = account.getDisplayName();
        }
        intent.putExtra("userName", String.format("Your Profile (%s)", name));
        intent.putExtra("userID", User.CURRENT_USER_ID);
        //intent.putExtra("loggedInUser", true);
        startActivity(intent);
    }


    public void showNotificationActivity(MenuItem item) {
        Intent intent = new Intent(MapsActivity.this, NotificationActivity.class);
        startActivity(intent);
    }

    public void showFollowRequestsActivity(MenuItem item) {
        Intent intent = new Intent(MapsActivity.this, FollowRequestsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_linearlayout);
//        setContentView(R.layout.activity_maps);

        mapFragment = new MapFragment();

        apiClient = new RestAPIClient(MapsActivity.this, null, null);

        Toolbar toolbar = findViewById(R.id.activity_maps_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayShowTitleEnabled(false);

        viewPager = findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        NavigationView navigationView = findViewById(R.id.nav_view);
        MenuItem menuItem = navigationView.getMenu().getItem(0);
        if(GoogleAccountSingleton.mGoogleSignInAccount != null){
            String menu_title = String.format("%s",
                    GoogleAccountSingleton.mGoogleSignInAccount.getEmail()
            );
            menuItem.setTitle(menu_title);
        }
        navigationView.setNavigationItemSelectedListener(this);

        mGeoDataClient = Places.getGeoDataClient(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createNotificationChannels();

        initUserAccount();
        drawer_init();
    }

    private void drawer_init(){
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        if (drawerLayout != null){
            View view = drawerLayout.findViewById(R.id.menu_drawer);
        }
    }

    private void initUserAccount() {
        apiClient.getCurrentUserID();
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        apiClient.updateDeviceToken(task.getResult().getToken());
                    }
                });
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannels() {
        String channelId = getResources().getString(R.string.notification_channel_comment);
        String channelName = "Comment";
        NotificationChannel channelComment =
                new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);

        channelId = getResources().getString(R.string.notification_channel_usertag);
        channelName = "UserTag";
        NotificationChannel channelUserTag =
                new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.createNotificationChannel(channelComment);
            manager.createNotificationChannel(channelUserTag);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(mapFragment, getString(R.string.maps_activity_tab_map));
        adapter.addFragment(new FeedFragment(), getString(R.string.maps_activity_tab_feed));
        viewPager.setAdapter(adapter);
    }

    public void openSearchActivity(View view) {
        Intent intent = new Intent(MapsActivity.this, MultiSearchActivity.class);

        if(mapFragment!=null && mapFragment.getmMap() != null){
            CameraPosition cameraPosition = mapFragment.getmMap().getCameraPosition();
            LatLng camera_target = cameraPosition.target;
            if(camera_target!=null){
                intent.putExtra(MultiSearchActivity.EXTRA_LOCATION, camera_target);
            }
        }
        startActivityForResult(intent, MapsActivity.MAP_MOVE_LOCATION);

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
            if(tabLayout!=null && viewPager!=null){
                ViewPagerAdapter viewPagerAdapter = (ViewPagerAdapter) viewPager.getAdapter();
                tabLayout.setScrollPosition(0,0f,true);
                viewPager.setCurrentItem(0);
            }


        }

        if (requestCode == CREATE_POST_ACTIVITY) {
            mapFragment.refreshPosts();
        }
    }
    private void handle_map_location_change_request(String place_id){
        mGeoDataClient.getPlaceById(place_id).addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
                if (task.isSuccessful()) {
                    PlaceBufferResponse response = task.getResult();
                    if(response!=null && response.getCount() > 0){

                        Place place = response.get(0);
                        //this should depend on whether map view or list view is showing
                        Button button = findViewById(R.id.search_button);
                        button.setText(
                                String.format("%s", place.getName())
                        );

                        if(mapFragment!=null){
                            mapFragment.setMapLocationAndLoadPosts(place.getLatLng());

                        }
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