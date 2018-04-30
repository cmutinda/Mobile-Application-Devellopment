package com.evoke.central.evokecentral.model;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Castrol on 10/23/2017.
 */

public class ShowResponse2 {

    @SerializedName("results")
    @Expose
    private List<Show> shows;

    public List<Show> getShows(){
        return shows;
    }


    public void setShows(List<Show> results) {
        this.shows = shows;
    }


    public ShowResponse2(){}


}
