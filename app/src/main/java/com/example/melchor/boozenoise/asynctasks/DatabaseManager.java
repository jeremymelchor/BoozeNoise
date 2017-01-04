package com.example.melchor.boozenoise.asynctasks;

import android.os.AsyncTask;

import com.example.melchor.boozenoise.entities.Bar;
import com.example.melchor.boozenoise.entities.ListBars;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

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
     * @param listBars Object containing all bars found
     */
    private void writeToDatabase(ListBars listBars) {
        for (Bar bar : listBars.getResultsFromWebservice()) {
            Map<String, Object> updates = new HashMap<>();
            updates.put(bar.getPlace_id(), bar);
            databaseReference.updateChildren(updates);
        }
    }

    /**
     * Update a bar with its recorded decibels
     * @param bar bar to update (found with place_id)
     * @param decibel number of recorded decibels
     */
    private void updateBarDecibels(Bar bar, double decibel) {
        Map<String,Object> update = new HashMap<>();
        update.put("decibels",decibel);
        databaseReference.child(bar.getPlace_id()).updateChildren(update);
    }
}
