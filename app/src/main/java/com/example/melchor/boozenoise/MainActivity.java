package com.example.melchor.boozenoise;

import android.content.pm.PackageManager;
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

    private static final String TAG = signInActivity.class.getSimpleName();

    MediaRecorder mediaRecorder;
    String fileName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set Permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
            String[] permissions = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, permissions, PackageManager.PERMISSION_GRANTED);
        }
    }

    public void recordSound(View view) {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        fileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        fileName += "/audiorecordtest.3gp";
        Log.d(TAG,fileName);
        mediaRecorder.setOutputFile(fileName);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }

        float percentOfASecond = (float) 2000 / 1000.0f;
        int numSamplesRequired = (int) ((float) sampleRate * percentOfASecond);
        int bufferSize =
                determineCalculatedBufferSize(sampleRate, encoding,
                        numSamplesRequired);

        return doRecording(sampleRate, encoding, bufferSize,
                numSamplesRequired, DEFAULT_BUFFER_INCREASE_FACTOR);
        mediaRecorder.start();
    }
}
