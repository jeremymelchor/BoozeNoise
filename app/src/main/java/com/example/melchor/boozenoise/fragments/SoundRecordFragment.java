package com.example.melchor.boozenoise.fragments;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.melchor.boozenoise.R;
import com.example.melchor.boozenoise.activities.MainActivity;
import com.example.melchor.boozenoise.asynctasks.GetCurrentBar;
import com.example.melchor.boozenoise.entities.Bar;
import com.example.melchor.boozenoise.asynctasks.DatabaseManager;
import com.example.melchor.boozenoise.asynctasks.SoundRecorder;
import com.example.melchor.boozenoise.entities.ListBars;

import java.util.ArrayList;

public class SoundRecordFragment extends Fragment implements
        AdapterView.OnItemClickListener,
        View.OnClickListener,
        GetCurrentBar.OnBarsFetchedListener,
        SoundRecorder.OnSoundRecordedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ListView listView;
    private Bar myBar;
    private ListBars barsFetched;
    private TextView progressDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(com.example.melchor.boozenoise.R.layout.fragment_sound_record, container, false);

        // Set ListView
        listView = (ListView) view.findViewById(android.R.id.list);
        TextView emptyText = (TextView) view.findViewById(android.R.id.empty);
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, new ArrayList<>());
        listView.setEmptyView(emptyText);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(this);

        // Set listeners on buttons
        view.findViewById(R.id.record).setOnClickListener(this);
        view.findViewById(R.id.sendRecord).setOnClickListener(this);

        progressDB = (TextView) view.findViewById(R.id.progressDB);

        return view;
    }

    /**
     * Called when the fragment is visible on screen
     *
     * @param hidden false = fragment visible
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            // Set Permissions
            if (ContextCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
                String[] permissions = new String[]{android.Manifest.permission.RECORD_AUDIO};
                ActivityCompat.requestPermissions(this.getActivity(), permissions, PackageManager.PERMISSION_GRANTED);
            }
        }
    }

    /**************************************/
    /**         LISTVIEW EVENTS          **/
    /**************************************/

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        myBar = (Bar) adapterView.getAdapter().getItem(i);
    }

    /**************************************/
    /**          BUTTONS EVENTS          **/
    /**************************************/

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.record:
                new GetCurrentBar(this, null).execute();
                break;
            case R.id.sendRecord:
                DatabaseManager databaseManager = new DatabaseManager("update");
                databaseManager.execute(myBar, (double) 10);
                break;
        }
    }

    /**************************************/
    /**           CUSTOM EVENTS          **/
    /**************************************/

    @Override
    public void onBarsFetched(ListBars listBars) {
        if (listBars.getResultsFromWebservice().isEmpty()) Log.d(TAG, "CANCEL BECAUSE 0 BARS");
        else {
            new SoundRecorder(this,progressDB).execute();
            barsFetched = listBars;
        }
    }

    @Override
    public void onSoundRecorded() {
        ArrayList<Bar> listDisplayed = new ArrayList<>();

        for (Bar bar : barsFetched.getResultsFromWebservice())
            listDisplayed.add(bar);

        ArrayAdapter arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listDisplayed);
        listView.setAdapter(arrayAdapter);

    }
}
