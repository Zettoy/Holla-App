package com.holla.group1.holla;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Pair;
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

        ArrayList<Pair<String, String>> users = new ArrayList<>();
        users.add(new Pair<String, String>("John", "093490283"));
        users.add(new Pair<String, String>("Greg", "0934902833"));

        FollowRequestAdapter adapter = new FollowRequestAdapter(this, users);
        setListAdapter(adapter);
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
