package com.example.melchor.boozenoise;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabSelectedListener;

public class MapsActivity extends AppCompatActivity {

    private static final String TAG = MapsActivity.class.getSimpleName();
    MapsFragment mapsFragment;
    UserProfileFragment userProfileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mapsFragment = new MapsFragment();
        userProfileFragment = new UserProfileFragment();
        //setFragment(mapsFragment);


        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container,mapsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        BottomBar bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.setItemsFromMenu(R.menu.bottom_bar_navigation, new OnMenuTabSelectedListener() {
            @Override
            public void onMenuItemSelected(int itemId) {
                switch (itemId) {
                    case R.id.profile_item:
                        //setFragment();
                        break;
                    case R.id.map_item:
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.hide(userProfileFragment);
                        //fragmentTransaction.show(mapsFragment);
                        fragmentTransaction.add(R.id.fragment_container,mapsFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                    case R.id.record_item:
                        setFragment(userProfileFragment);
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
     * Set a fragment in the fragment_container
     *
     * @param fragment the fragment we want to display
     */
    public void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void setMapFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();
    }

}
