package com.evoke.central.evokecentral.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Castrol on 10/23/2017.
 */

public class Show {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("vote_average")
    @Expose
    private String rating;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("overview")
    @Expose
    private String description;

    @SerializedName("title")
    @Expose
    private String title;


    public Show() {



    }


    public Show(String name, String rating, String posterPath, String description){
        this.name = name;
        this.rating = rating;
        this.posterPath = posterPath;
        this.description = description;

    }

    String baseImageUrl = "https://image.tmdb.org/t/p/w500";

    public String getName(){return name;}
    public void setName(String name){ this.name = name; }

    public String getRating(){return rating;}
    public void setRating(String rating){ this.rating = rating; }

    public String getPosterPath(){return "https://image.tmdb.org/t/p/w500" + posterPath;}
    public void setPosterPath(String posterPath){ this.posterPath = posterPath; }

    public String getDescription(){return description;}
    public void setDescription(String description){ this.description = description; }

    public String getTitle(){return title;}
    public void setTitle(String title){ this.title = title; }

}
