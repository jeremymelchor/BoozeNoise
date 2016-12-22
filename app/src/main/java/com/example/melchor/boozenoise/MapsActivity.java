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
    private final MapsFragment mapsFragment = new MapsFragment();
    private final UserProfileFragment userProfileFragment = new UserProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container,mapsFragment);
        fragmentTransaction.add(R.id.fragment_container,userProfileFragment);
        fragmentTransaction.hide(userProfileFragment);
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
                        setFragment(mapsFragment);
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
     * Change display of fragment
     * @param fragment_to_show
     */
    public void setFragment(Fragment fragment_to_show) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        if (fragment_to_show == mapsFragment)
            fragmentTransaction.hide(userProfileFragment);
        else fragmentTransaction.hide(mapsFragment);

        fragmentTransaction.show(fragment_to_show);
        fragmentTransaction.commit();
    }

}
