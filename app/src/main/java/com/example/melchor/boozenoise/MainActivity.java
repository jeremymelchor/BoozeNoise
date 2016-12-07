package com.example.melchor.boozenoise;

import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.Manifest;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();


    private AudioRecord audioRecord;
    private int bufferSize;
    private double lastLevel;
    private Thread thread;
    private final int SAMPLE_DELAY = 2000;
    private final int SAMPLE_RATE = 8000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set Permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
            String[] permissions = new String[]{Manifest.permission.RECORD_AUDIO};
            ActivityCompat.requestPermissions(this, permissions, PackageManager.PERMISSION_GRANTED);
        }

        try {
            bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
        } catch (Exception e) {
            Log.e("TrackingFlow", "Exception", e);
        }

    }

    /**
     * See http://www.doepiccoding.com/blog/?p=195
     * @param view
     * @throws IOException
     */
    public void recordSound(View view) throws IOException {
        audioRecord.startRecording();

        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while ((thread != null) && !thread.isInterrupted()) {
                    //Let's make the thread sleep for a the approximate sampling time
                    try {
                        Thread.sleep(SAMPLE_DELAY);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                    readAudioBuffer();//After this call we can get the last value assigned to the lastLevel variable

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "DB : " + lastLevel);
                        }
                    });
                }
            }
        });

        thread.start();
    }

    /**
     * Functionality that gets the sound level out of the sample
     */
    private void readAudioBuffer() {

        try {
            short[] buffer = new short[bufferSize];
            int bufferReadResult;

            if (audioRecord != null) {

                bufferReadResult = audioRecord.read(buffer, 0, bufferSize);
                /*for (int i=0; i<buffer.length; i++)
                    Log.d(TAG,"buffer["+i+"] : " + buffer[i]);*/
                double sumLevel = 0;
                for (int i = 0; i < bufferReadResult; i++) {
                    if (buffer[i] != 0)
                        sumLevel += 20*Math.log10(((double) Math.abs(buffer[i]))/65535.0);
                }
                lastLevel = Math.abs((sumLevel / bufferReadResult));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
