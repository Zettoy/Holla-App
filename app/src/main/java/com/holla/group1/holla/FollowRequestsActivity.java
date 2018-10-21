package com.holla.group1.holla;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.holla.group1.holla.R;

import java.util.ArrayList;
import java.util.List;

public class FollowRequestsActivity extends ListActivity {
    //private TextView text;
    private List<String> listValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_requests);

        //text = (TextView) findViewById(R.id.mainText);

        listValues = new ArrayList<String>();
        listValues.add("Android");
        listValues.add("iOS");
        listValues.add("Symbian");
        listValues.add("Blackberry");
        listValues.add("Windows Phone");

        // initiate the listadapter
        ArrayAdapter<String> myAdapter = new ArrayAdapter <String>(this,
                R.layout.row_layout_follow_request, R.id.request_username_txt, listValues);

        // assign the list adapter
        setListAdapter(myAdapter);

    }

    // when an item of the list is clicked
    /*@Override
    protected void onListItemClick(ListView list, View view, int position, long id) {
        super.onListItemClick(list, view, position, id);

        String selectedItem = (String) getListView().getItemAtPosition(position);
        //String selectedItem = (String) getListAdapter().getItem(position);

        text.setText("You clicked " + selectedItem + " at position " + position);
    }*/

}
