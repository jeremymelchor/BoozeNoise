package com.example.melchor.boozenoise.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.melchor.boozenoise.Data;
import com.example.melchor.boozenoise.entities.Bar;
import com.example.melchor.boozenoise.entities.Geometry;
import com.example.melchor.boozenoise.entities.ListBars;
import com.example.melchor.boozenoise.entities.Location;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DatabaseManager extends AsyncTask<Object, Void, Void> {

    private static final String TAG = DatabaseManager.class.getSimpleName();

    private DatabaseReference databaseReference;
    private String databaseAction;

    public DatabaseManager(String databaseAction) {
        this.databaseAction = databaseAction;
    }

    @Override
    protected void onPreExecute() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("bars");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "value change : " + dataSnapshot);
                HashMap<String, Bar> hashmap = (HashMap<String, Bar>) dataSnapshot.getValue();
                ArrayList<Bar> resultsFromWebservice = new ArrayList<>();
                // Getting a Set of Key-value pairs
                Set entrySet = hashmap.entrySet();

                // Obtaining an iterator for the entry set
                Iterator it = entrySet.iterator();

                // Iterate through HashMap entries(Key-Value pairs)
                System.out.println("HashMap Key-Value Pairs : ");
                while (it.hasNext()) {
                    Map.Entry me = (Map.Entry) it.next();
                    System.out.println("Key is: " + me.getKey() + " & " + " value is: " + me.getValue());
                    HashMap<String, String> hashMapBar = (HashMap<String, String>) me.getValue();
                    HashMap<String, HashMap<String, HashMap<String, Double>>> map = (HashMap<String, HashMap<String, HashMap<String, Double>>>) me.getValue();
                    Bar b = new Bar();
                    Double latitude = map.get("geometry").get("location").get("latitude");
                    Double longitude = map.get("geometry").get("location").get("longitude");
                    Location l = new Location(latitude, longitude);
                    Geometry g = new Geometry(l);
                    b.setGeometry(g);
                    b.setPlace_id(hashMapBar.get("place_id"));
                    b.setName(hashMapBar.get("name"));
                    b.setRating((float) Double.parseDouble(String.valueOf(hashMapBar.get("rating"))));
                    b.setVicinity(hashMapBar.get("vicinity"));
                    b.setDecibels((long) Double.parseDouble(String.valueOf(hashMapBar.get("decibels"))));
                    resultsFromWebservice.add(b);
                }
                Data.setListBars(resultsFromWebservice);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected Void doInBackground(Object... param) {
        switch (databaseAction) {
            case "write":
                writeToDatabase((ListBars) param[0]);
                break;
            case "update":
                updateBarDecibels((Bar) param[0], (double) param[1]);
                break;
        }
        return null;
    }

    /**************************************/
    /**             FUNCTIONS            **/
    /**************************************/

    /**
     * put all bars in listBars#getResultsFromWebService in the DB
     *
     * @param listBars Object containing all bars found
     */
    private void writeToDatabase(ListBars listBars) {
        for (final Bar bar : listBars.getResultsFromWebservice()) {

            databaseReference.child(bar.getPlace_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Bar already exist
                        //Log.d(TAG, "BAR EXISTS" + bar.getName());
                    } else {
                        // Bar doesn't exist yet
                        //Log.d(TAG, "BAR NOT EXISTS");
                        databaseReference.child(bar.getPlace_id()).setValue(bar);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    /**
     * Update a bar with its recorded decibels
     *
     * @param bar     bar to update (found with place_id)
     * @param decibel number of recorded decibels
     */
    private void updateBarDecibels(Bar bar, double decibel) {
        Map<String, Object> update = new HashMap<>();
        update.put("decibels", decibel);
        databaseReference.child(bar.getPlace_id()).updateChildren(update);
    }
}
