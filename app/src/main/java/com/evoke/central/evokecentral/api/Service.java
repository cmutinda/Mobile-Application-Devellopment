package com.evoke.central.evokecentral.api;

import com.evoke.central.evokecentral.model.ShowResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Castrol on 10/23/2017.
 */

public interface Service {
    @GET("tv/airing_today?api_key=ffba9e1453c2cc7648d6e8bede28fe6d&language=en-US")
    Call<ShowResponse> getShows();

}
