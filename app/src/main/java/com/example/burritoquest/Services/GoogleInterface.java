package com.example.burritoquest.Services;

import com.example.burritoquest.Model.GoogleResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleInterface {
    @GET("maps/api/place/nearbysearch/json")
    Call<GoogleResult> googleResultCall(@Query("pageToken") String pageToken, @Query("radius") Integer radius, @Query("type") String type, @Query("keyword") String keyword,
                                        @Query("key") String apiKey, @Query("location") String location);

    @GET("maps/api/place/nearbysearch/json")
    Call<GoogleResult> secondResultCall(@Query("pageToken") String pageToken, @Query("key") String apiKey);

}
