package com.example.melchor.boozenoise.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.melchor.boozenoise.R;

public class Listfragment extends ListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final String[] items = {"France","Angleterre","France","Angleterre","France","Angleterre","France","Angleterre","France","Angleterre","France","Angleterre","France","Angleterre","France","Angleterre","France","Angleterre"};
        final ArrayAdapter<String> aa = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, items);

        setListAdapter(aa);
    }
}
