package com.example.melchor.boozenoise.fragments;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.melchor.boozenoise.HttpRequest;
import com.example.melchor.boozenoise.R;
import com.example.melchor.boozenoise.activities.MainActivity;
import com.example.melchor.boozenoise.entities.Bar;
import com.example.melchor.boozenoise.asynctasks.DatabaseManager;
import com.example.melchor.boozenoise.asynctasks.SoundRecorder;
import com.example.melchor.boozenoise.entities.ListBars;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class SoundRecordFragment extends Fragment implements
        AdapterView.OnItemClickListener,
        View.OnClickListener,
        SoundRecorder.OnSoundRecordedListener,
        HttpRequest.HttpRequestListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ListView listView;
    private Bar myBar;
    private ListBars barsFetched;
    private TextView progress;
    private Button record, sendRecord;

    ListBars listBars;

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

        sendRecord.setVisibility(View.INVISIBLE);

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

    //==============================================================================================
    // Listeners implementation
    //==============================================================================================

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sound_record_record:
                new HttpRequest(getContext(), this).getBarsAroundMe(50);
                break;
            case R.id.sound_record_send_data:
                DatabaseManager databaseManager = new DatabaseManager("update",null);
                databaseManager.execute(myBar, Double.parseDouble(progress.getText().toString()));
                sendRecord.setVisibility(View.INVISIBLE);
                progress.setText("0");
                break;
        }
    }

    /**
     * When we click on a bar in the list, we get the entity by his name
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        for (Bar bar : listBars.getResultsFromWebservice()) {
            if (bar.getName() == adapterView.getAdapter().getItem(i))
                myBar = bar;
        }
    }

    /**
     * Event fired when the asynctask SoundRecorder has recorded the ambiant sound
     */
    @Override
    public void onSoundRecorded() {
        ArrayList<String> listDisplayed = new ArrayList<>();

        for (Bar bar : barsFetched.getResultsFromWebservice())
            listDisplayed.add(bar.getName());

        ArrayAdapter arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listDisplayed);
        listView.setAdapter(arrayAdapter);

        sendRecord.setVisibility(View.VISIBLE);
    }

    /**
     * Event fired when we receive the (close) bars fetched from the webservice
     * with a radius distance of 50 meters
     * @param response from the webservice
     * @throws JSONException
     */
    @Override
    public void onBarsAroundMeFetched(JSONArray response) throws JSONException {
        Gson gson = new GsonBuilder().create();
        listBars = gson.fromJson(response.get(0).toString(), ListBars.class);
        if (listBars.getResultsFromWebservice().isEmpty()) {
            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setTitle("Oups !");
            alertDialog.setMessage("Nous n'avons pas pu vous localiser dans un bar");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        else {
            new SoundRecorder(this, progress).execute();
            barsFetched = listBars;
        }
    }
}
