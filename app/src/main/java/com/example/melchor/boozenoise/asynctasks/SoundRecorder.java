package com.example.melchor.boozenoise.asynctasks;

import android.icu.text.DecimalFormat;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.example.melchor.boozenoise.Data;

/**
 * See http://www.doepiccoding.com/blog/?p=195
 */
public class SoundRecorder extends AsyncTask<Void, Double, Double> {

    private static final String TAG = SoundRecorder.class.getSimpleName();

    private AudioRecord audioRecord;
    private int bufferSize;
    private final OnSoundRecordedListener listener;
    private TextView textProgress;

    public SoundRecorder(OnSoundRecordedListener listener, TextView textProgress) {
        this.listener = listener;
        this.textProgress = textProgress;
    }

    @Override
    protected void onPreExecute() {
        bufferSize = AudioRecord.getMinBufferSize(Data.getSampleRate(), AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, Data.getSampleRate(), AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
    }

    @Override
    protected Double doInBackground(Void... params) {

        audioRecord.startRecording();

        double averageDB = 0, readDB, nbSamples = 0;
        while (nbSamples < Data.getAudioSamples()) {
            //Let's make the thread sleep for a the approximate sampling time
            try {
                Thread.sleep(Data.getSampleDelay());
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }

            readDB = readAudioBuffer();
            publishProgress(readDB);

            averageDB += readAudioBuffer();
            nbSamples++;
            Log.d(TAG, "Nb samples : " + nbSamples);

        }

        // End of recording
        return averageDB / Data.getAudioSamples();
    }

    @Override
    protected void onProgressUpdate(Double... values) {
        textProgress.setText(String.valueOf(Math.round(values[0])));
    }

    @Override
    protected void onPostExecute(Double averageDB) {
        textProgress.setText(String.valueOf(Math.round(averageDB)));
        if (listener != null) listener.onSoundRecorded();
    }

    //==============================================================================================
    // Utils functions implementation
    //==============================================================================================

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

    //==============================================================================================
    // Callback Interface
    //==============================================================================================

    public interface OnSoundRecordedListener {
        void onSoundRecorded();
    }
}
