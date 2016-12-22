package com.example.melchor.boozenoise.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.example.melchor.boozenoise.R;
import com.example.melchor.boozenoise.fragments.BottomSheetFragment;
import com.example.melchor.boozenoise.fragments.MapsFragment;
import com.example.melchor.boozenoise.fragments.SoundRecordFragment;
import com.example.melchor.boozenoise.fragments.UserProfileFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabSelectedListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private final MapsFragment mapsFragment = new MapsFragment();
    private final UserProfileFragment userProfileFragment = new UserProfileFragment();
    private final SoundRecordFragment soundRecordFragment = new SoundRecordFragment();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Add all fragments and set maps fragment visibility
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container,userProfileFragment);
        fragmentTransaction.add(R.id.fragment_container,mapsFragment);
        fragmentTransaction.add(R.id.fragment_container,soundRecordFragment);
        fragmentTransaction.hide(userProfileFragment);
        fragmentTransaction.hide(soundRecordFragment);
        fragmentTransaction.commit();

        BottomBar bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.setItemsFromMenu(R.menu.bottom_bar_navigation, new OnMenuTabSelectedListener() {
            @Override
            public void onMenuItemSelected(int itemId) {
                switch (itemId) {
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
            }
        });
        // Set the color for the active tab. Ignored on mobile when there are more than three tabs.
        bottomBar.setActiveTabColor("#C2185B");
    }


    /**************************************/
    /**              EVENTS              **/
    /**************************************/

    /**************************************/
    /**            FUNCTIONS             **/
    /**************************************/

    /**
     * Change display of fragment
     * @param fragment_to_show
     */
    public void setFragment(Fragment fragment_to_show) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        if (fragment_to_show == userProfileFragment) {
            fragmentTransaction.hide(mapsFragment);
            fragmentTransaction.hide(soundRecordFragment);
        }
        else if (fragment_to_show == mapsFragment) {
            fragmentTransaction.hide(userProfileFragment);
            fragmentTransaction.hide(soundRecordFragment);
        }
        else {
            fragmentTransaction.hide(userProfileFragment);
            fragmentTransaction.hide(mapsFragment);
        }

        fragmentTransaction.show(fragment_to_show);
        fragmentTransaction.commit();
    }

}
