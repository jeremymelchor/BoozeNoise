package com.example.melchor.boozenoise.utils;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.util.Log;

/**
 * See http://www.doepiccoding.com/blog/?p=195
 */
public class SoundRecorder extends AsyncTask<Void, Void, Void> {

    private static final String TAG = SoundRecorder.class.getSimpleName();
    private final int SAMPLE_DELAY = 2000;
    private final int SAMPLE_RATE = 8000;
    private final int AUDIO_SAMPLES = 10;

    private AudioRecord audioRecord;
    private int bufferSize;

    @Override
    protected void onPreExecute() {
        bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
    }

    @Override
    protected Void doInBackground(Void... params) {

        audioRecord.startRecording();

        double averageDB = 0, nbSamples = 0;
        while (nbSamples < AUDIO_SAMPLES) {
            //Let's make the thread sleep for a the approximate sampling time
            try {
                Thread.sleep(SAMPLE_DELAY);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }

            averageDB += readAudioBuffer();//After this call we can get the last value assigned to the lastLevel variable
            nbSamples++;
            Log.d(TAG,"Nb samples : "+nbSamples);

        }

        // End of recording
        Log.d(TAG, "average DB : " + averageDB / AUDIO_SAMPLES);

        return null;
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
}
