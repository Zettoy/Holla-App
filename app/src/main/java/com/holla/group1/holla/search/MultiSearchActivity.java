package com.holla.group1.holla.search;

import android.Manifest;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.holla.group1.holla.MapsActivity;
import com.holla.group1.holla.R;
import com.holla.group1.holla.api.SearchUsersRequest;
import com.holla.group1.holla.search.location.LocationSearchResult;
import com.holla.group1.holla.search.location.LocationSearchResultFragment;
import com.holla.group1.holla.search.post.PostSearchFragment;
import com.holla.group1.holla.search.user.UserSearchResultFragment;
import com.holla.group1.holla.user.User;

import java.util.ArrayList;
import java.util.List;

public class MultiSearchActivity extends AppCompatActivity implements
        SearchView.OnQueryTextListener,
        OnCompleteListener<AutocompletePredictionBufferResponse>,
        OnFailureListener,
        LocationSearchResultFragment.OnListFragmentInteractionListener,
        UserSearchResultFragment.OnListFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private static final String TAG = "MultiSearchActivity";

    private static final int TAB_PEOPLE = 0;
    private static final int TAB_PLACES = 1;
    private static final int TAB_POSTS = 2;
    private static final int DEFAULT_TAB = TAB_PEOPLE;
    protected GeoDataClient mGeoDataClient;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private FusedLocationProviderClient mFusedLocationClient;

    private void search_by_post_query(final String query) {
        Fragment cur_fragment = getCurrentFragment();
        if(cur_fragment instanceof PostSearchFragment) {
            PostSearchFragment postSearchFragment = (PostSearchFragment) cur_fragment;
            postSearchFragment.search(query);
        }
    }
    private void search_by_username(final String query) {
        SearchUsersRequest request = new SearchUsersRequest(this);
        request.setListener(new SearchUsersRequest.ResponseListener() {
            @Override
            public void onSearchUsersResponse(List<User> users) {

                Fragment cur_fragment = getCurrentFragment();
                if(cur_fragment instanceof UserSearchResultFragment) {
                    UserSearchResultFragment userSearchResultFragment = (UserSearchResultFragment) cur_fragment;
                    userSearchResultFragment.showResults(users);
//                    LocationSearchResultFragment locationSearchResultFragment = (LocationSearchResultFragment) cur_fragment;
//                    locationSearchResultFragment.showResults(list);
                }
            }

            @Override
            public void onSearchUsersError(Exception ex) {

            }
        });
        request.getUsersByUsername(query);
    }

    private void search_by_location(final String query) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more location_name.
            return;
        }
        mFusedLocationClient.getLastLocation().addOnCompleteListener(
                new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {

                        Location loc = task.getResult();
                        LatLngBounds.Builder builder = LatLngBounds.builder().include(new LatLng(loc.getLatitude(), loc.getLongitude()));
                        LatLngBounds dynamic_bounds = builder.build();

                        LatLngBounds syd_cbd_bounds = new LatLngBounds(new LatLng(
                                -33.880208, 151.188317

                        ), new LatLng(-33.857112, 151.218716));
                        Task<AutocompletePredictionBufferResponse> prediction_task = mGeoDataClient.getAutocompletePredictions(query, dynamic_bounds, null);
                        prediction_task.addOnCompleteListener(MultiSearchActivity.this);
                        prediction_task.addOnFailureListener(MultiSearchActivity.this);
                    }
                }
        );

    }

    private void handleSearchQuery(String query) {
        if (mViewPager != null) {
            Integer cur = mViewPager.getCurrentItem();
            switch (cur) {
                case TAB_PEOPLE:
                    search_by_username(query);
                    break;

                case TAB_PLACES:
                    search_by_location(query);

                    break;

                case TAB_POSTS:
                    search_by_post_query(query);
                    break;
            }
        }

    }

    public boolean onQueryTextSubmit(String s) {
        handleSearchQuery(s);
        return false;
    }

    public boolean onQueryTextChange(String s) {
        if(!(getCurrentFragment() instanceof PostSearchFragment)){
            handleSearchQuery(s);
        }
        return false;
    }

    @Override
    public void onListFragmentInteraction(LocationSearchResult.Item item) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(MapsActivity.EXTRA_PLACE_ID, item.place_id);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onListFragmentInteraction(User user) {
//        finish();
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        ;
    }

    @Override
    public void onComplete(@NonNull Task<AutocompletePredictionBufferResponse> task) {
        AutocompletePredictionBufferResponse response = task.getResult();
        List<LocationSearchResult.Item> items = new ArrayList<>();
        for (AutocompletePrediction x : response) {
//            Log.d(TAG, x.getFullText(null).toString());
            LocationSearchResult.Item item = new LocationSearchResult.Item(x.getPlaceId(), x.getFullText(null).toString());
            items.add(item);
        }
        response.release();
        displayLocationSearchResults(items);

    }

    private Fragment getCurrentFragment() {

        SectionsPagerAdapter adapter = (SectionsPagerAdapter) mViewPager.getAdapter();

        if (adapter != null) {
            Fragment fragment = adapter.getCurrentFragment();
            return fragment;
        }
        return null;
    }

    private void displayLocationSearchResults(List<LocationSearchResult.Item> list) {
        Fragment cur_fragment = getCurrentFragment();
        if(cur_fragment instanceof LocationSearchResultFragment){
            LocationSearchResultFragment locationSearchResultFragment = (LocationSearchResultFragment) cur_fragment;
            locationSearchResultFragment.showResults(list);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                Fragment cur_frag = getCurrentFragment();

            }

            @Override
            public void onPageSelected(int i) {
                Fragment cur_frag = getCurrentFragment();

            }

            @Override
            public void onPageScrollStateChanged(int i) {
                Fragment cur_frag = getCurrentFragment();
                if(MultiSearchActivity.this.searchView != null){
                    String query = MultiSearchActivity.this.searchView.getQuery().toString();
                    handleSearchQuery(query);
                }

            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


        mViewPager.setCurrentItem(DEFAULT_TAB);
        mGeoDataClient = Places.getGeoDataClient(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }



    private SearchView searchView;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_multi_search, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        this.searchView=searchView;
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.requestFocus();
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private Fragment mCurrentFragment;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getCurrentFragment() {
            return mCurrentFragment;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == TAB_PLACES) {
                LocationSearchResultFragment loc_frag = LocationSearchResultFragment.newInstance();
                return loc_frag;

            } else if (position == TAB_PEOPLE) {
                UserSearchResultFragment user_fragment = UserSearchResultFragment.newInstance();
                return user_fragment;

            } else if (position == TAB_POSTS) {
                PostSearchFragment postSearchFragment = new PostSearchFragment();
                return postSearchFragment;
            }else{
                return null;
            }
        }

        @Override
        public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            if (getCurrentFragment() != object) {
                mCurrentFragment = (Fragment) object;
            }
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
