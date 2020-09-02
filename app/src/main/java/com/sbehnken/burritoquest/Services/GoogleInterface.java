package com.sbehnken.burritoquest.Services;

import com.sbehnken.burritoquest.Model.GoogleResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleInterface {
    @GET("maps/api/place/nearbysearch/json")
    Call<GoogleResult> firstGoogleResultCall(@Query("radius") Integer radius, @Query("type") String type, @Query("keyword") String keyword,
                                             @Query("key") String apiKey, @Query("location") String location, @Query("pagetoken") String pageToken);


}
