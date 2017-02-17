package com.example.melchor.boozenoise.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.melchor.boozenoise.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfileFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;

    TextView userNameView;
    TextView userEmailView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(com.example.melchor.boozenoise.R.layout.fragment_sound_record, container, false);

        userNameView = (TextView) view.findViewById(R.id.userName);
        userEmailView = (TextView) view.findViewById(R.id.userEmail);

        auth = FirebaseAuth.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                userNameView.setText(user.getDisplayName());
                userEmailView.setText(user.getEmail());
            }
        };


        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }
}
