package com.example.melchor.boozenoise.utils;

import android.os.AsyncTask;

import com.example.melchor.boozenoise.entities.Bar;
import com.example.melchor.boozenoise.entities.ListBars;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Map;

public class DatabaseManager extends AsyncTask<String, Void, Void> {

    private static final String TAG = DatabaseManager.class.getSimpleName();
    private String databaseAction;
    private ListBars listBars;
    private ChildEventListener childEventListener;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    public DatabaseManager(String databaseAction, ListBars listBars) {
        this.databaseAction = databaseAction;
        this.listBars = listBars;
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("bars");
    }

    @Override
    protected Void doInBackground(String... param) {
        if (databaseAction.equals("write")) writeToDatabase();
        return null;
    }

    private void writeToDatabase() {
        for (Bar bar : listBars.getResultsFromWebservice()) {
            Map<String, Object> updates = new HashMap<>();
            updates.put(bar.getPlace_id(), bar);
            databaseReference.updateChildren(updates);
        }
    }
}
