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
import com.example.melchor.boozenoise.utils.GetCurrentBar;

import java.io.IOException;
import java.util.ArrayList;

public class SoundRecordFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();


    private AudioRecord audioRecord;
    private int bufferSize;
    private Thread thread;
    private final int SAMPLE_DELAY = 2000;
    private final int SAMPLE_RATE = 8000;
    private final int AUDIO_SAMPLES = 10;

    private DataCommunication dataCommunication;
    private ArrayAdapter arrayAdapter;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(com.example.melchor.boozenoise.R.layout.fragment_sound_record, container, false);

        // Set ListView
        listView = (ListView) view.findViewById(android.R.id.list);
        TextView emptyText = (TextView) view.findViewById(android.R.id.empty);
        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, new ArrayList<String>());
        listView.setEmptyView(emptyText);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(this);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        try {
            bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
        } catch (Exception e) {
            Log.e("TrackingFlow", "Exception", e);
        }

        /** Set view listeners **/
        view.findViewById(R.id.record).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    recordSound(v);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        view.findViewById(R.id.getCurrentBar).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new GetCurrentBar(dataCommunication.getLatitude(), dataCommunication.getLongitude(), new GetCurrentBar.OnBarsFetched() {
                    @Override
                    public void onBarsFetched(ArrayList<String> listBars) {
                        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listBars);
                        listView.setAdapter(arrayAdapter);
                    }
                }).execute();
            }
        });

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
    /**            FUNCTIONS             **/
    /**************************************/

    /**
     * See http://www.doepiccoding.com/blog/?p=195
     *
     * @param view
     * @throws IOException
     */
    public void recordSound(View view) throws IOException {
        audioRecord.startRecording();

        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                double averageDB = 0, nbSamples = 0;
                while ((thread != null) && !thread.isInterrupted()) {
                    //Let's make the thread sleep for a the approximate sampling time
                    try {
                        Thread.sleep(SAMPLE_DELAY);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                    averageDB += readAudioBuffer();//After this call we can get the last value assigned to the lastLevel variable

                    // End of recording
                    if (++nbSamples == AUDIO_SAMPLES) {
                        Log.d(TAG, "average DB : " + averageDB / AUDIO_SAMPLES);

                        thread.interrupt();
                    }

                    //todo: mettre dans la DB


                    /*runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "DB : " + mediumDB);
                        }
                    });*/
                }
            }
        });

        thread.start();
    }

    /**
     * Functionality that gets the sound level out of the sample
     */
    private double readAudioBuffer() {
        double lastLevel = 0;

        try {
            short[] buffer = new short[bufferSize];
            int bufferReadResult;

            if (audioRecord != null) {

                bufferReadResult = audioRecord.read(buffer, 0, bufferSize);
                double sumLevel = 0;
                for (int i = 0; i < bufferReadResult; i++) {
                    if (buffer[i] != 0)
                        sumLevel += 20 * Math.log10(((double) Math.abs(buffer[i])) / 65535.0);
                }
                lastLevel = Math.abs((sumLevel / bufferReadResult));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lastLevel;
    }

    /**************************************/
    /**         LISTVIEW EVENTS          **/
    /**************************************/

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
       Log.d(TAG,""+adapterView.getAdapter().getItem(i));
    }

}
