package com.example.melchor.boozenoise.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ListBars {

    @SerializedName("results")
    private ArrayList<Bar> resultsFromWebservice;

    public ArrayList<Bar> getResultsFromWebservice() {
        return resultsFromWebservice;
    }

    @Override
    public String toString() {
        return resultsFromWebservice.toString();
    }
}
