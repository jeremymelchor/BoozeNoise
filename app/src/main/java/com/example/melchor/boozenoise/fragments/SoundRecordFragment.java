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
import android.view.ViewPropertyAnimator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    private TextView progress;
    private Button record, sendRecord;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(com.example.melchor.boozenoise.R.layout.fragment_sound_record, container, false);

        // Set view elements
        record = (Button) view.findViewById(R.id.sound_record_record);
        sendRecord = (Button) view.findViewById(R.id.sound_record_send_data);
        progress = (TextView) view.findViewById(R.id.sound_record_progress);

        // Set ListView
        listView = (ListView) view.findViewById(R.id.sound_record_list_bars);
        TextView emptyText = (TextView) view.findViewById(R.id.sound_record_list_empty);
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, new ArrayList<>());
        listView.setEmptyView(emptyText);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(this);

        // Set listeners on buttons
        record.setOnClickListener(this);
        sendRecord.setOnClickListener(this);

        // Set initial elements state
        sendRecord.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.INVISIBLE);

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
            case R.id.sound_record_record:
                Animation slideinAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, -40);
                slideinAnimation.setDuration(1000);
                record.startAnimation(slideinAnimation);
                //record.animate().alpha(0).setDuration(1000).start();
                //new GetCurrentBar(this, getContext()).execute();
                break;
            case R.id.sound_record_send_data:
                DatabaseManager databaseManager = new DatabaseManager("update");
                databaseManager.execute(myBar, (double) 10);
                break;
        }
    }

    //==============================================================================================
    // Listeners implementation
    //==============================================================================================

    /**
     * We launch the recorder only if there's at least 1 bar around us
     * @param listBars fetched from GetCurrentBar
     */
    @Override
    public void onBarsFetched(ListBars listBars) {
        if (listBars.getResultsFromWebservice().isEmpty()) Log.d(TAG, "CANCEL BECAUSE 0 BARS");
        else {
            new SoundRecorder(this, progress).execute();
            barsFetched = listBars;
        }
    }

    @Override
    public void onSoundRecorded() {
        progress.animate().translationY(0).setDuration(1000).start();


        ArrayList<String> listDisplayed = new ArrayList<>();

        for (Bar bar : barsFetched.getResultsFromWebservice())
            listDisplayed.add(bar.getName());

        ArrayAdapter arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listDisplayed);
        listView.setAdapter(arrayAdapter);
        listView.setVisibility(View.VISIBLE);
    }
}
