package com.example.melchor.boozenoise.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.melchor.boozenoise.DataCommunication;
import com.example.melchor.boozenoise.R;
import com.example.melchor.boozenoise.activities.MainActivity;
import com.example.melchor.boozenoise.entities.Bar;
import com.example.melchor.boozenoise.utils.DatabaseManager;
import com.example.melchor.boozenoise.utils.GetCurrentBar;
import com.example.melchor.boozenoise.utils.SoundRecorder;

import java.io.IOException;
import java.util.ArrayList;

public class SoundRecordFragment extends Fragment implements
        AdapterView.OnItemClickListener,
        View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private DataCommunication dataCommunication;
    private ListView listView;
    private Bar myBar;

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


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            dataCommunication = (DataCommunication) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement DataCommunication");
        }
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
                new SoundRecorder().execute();
                break;
            case R.id.sendRecord:
                DatabaseManager databaseManager = new DatabaseManager("update");
                databaseManager.execute(myBar, (double) 10);
                break;
        }
    }
}
