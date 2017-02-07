package com.example.melchor.boozenoise.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ListBars {

    @SerializedName("results")
    private ArrayList<Bar> resultsFromWebservice;

    private String status;

    public ArrayList<Bar> getResultsFromWebservice() {
        return resultsFromWebservice;
    }

    public String getStatus() { return status; }


}
