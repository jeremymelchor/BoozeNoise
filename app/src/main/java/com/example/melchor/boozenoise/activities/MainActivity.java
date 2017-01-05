package com.example.melchor.boozenoise.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.melchor.boozenoise.R;
import com.example.melchor.boozenoise.fragments.MapsFragment;
import com.example.melchor.boozenoise.fragments.SoundRecordFragment;
import com.example.melchor.boozenoise.fragments.UserProfileFragment;
import com.google.firebase.FirebaseApp;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private final MapsFragment mapsFragment = new MapsFragment();
    private final UserProfileFragment userProfileFragment = new UserProfileFragment();
    private final SoundRecordFragment soundRecordFragment = new SoundRecordFragment();


    /**************************************/
    /**              EVENTS              **/
    /**************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize Firebase
        FirebaseApp.initializeApp(this);

        // Add all fragments and set maps fragment visibility
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, userProfileFragment);
        fragmentTransaction.add(R.id.fragment_container, mapsFragment);
        fragmentTransaction.add(R.id.fragment_container, soundRecordFragment);
        fragmentTransaction.hide(userProfileFragment);
        fragmentTransaction.hide(soundRecordFragment);
        fragmentTransaction.commit();

        // Set listeners on menu icons
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile_item:
                        setFragment(userProfileFragment);
                        break;
                    case R.id.map_item:
                        setFragment(mapsFragment);
                        break;
                    case R.id.record_item:
                        setFragment(soundRecordFragment);
                        break;
                }
                return false;
            }
        });

    }

    /**************************************/
    /**            FUNCTIONS             **/
    /**************************************/

    /**
     * Change display of fragment
     *
     * @param fragment_to_show
     */
    public void setFragment(Fragment fragment_to_show) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        if (fragment_to_show == userProfileFragment) {
            fragmentTransaction.hide(mapsFragment);
            fragmentTransaction.hide(soundRecordFragment);
        } else if (fragment_to_show == mapsFragment) {
            fragmentTransaction.hide(userProfileFragment);
            fragmentTransaction.hide(soundRecordFragment);
        } else {
            fragmentTransaction.hide(userProfileFragment);
            fragmentTransaction.hide(mapsFragment);
        }

        fragmentTransaction.show(fragment_to_show);
        fragmentTransaction.commit();
    }
}
