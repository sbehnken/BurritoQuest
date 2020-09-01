package com.example.burritoquest;

import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.burritoquest.Model.GoogleResult;
import com.example.burritoquest.Model.Result;
import com.example.burritoquest.Services.GoogleService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantDataSource extends PageKeyedDataSource<String, Result> {
    private final GoogleService googleService = new GoogleService();
    private String nextPageToken;
    private String location;

    public RestaurantDataSource(String loc) {
        location = loc;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull final LoadInitialCallback<String, Result> callback) {
        googleService.googleResultCall(location, nextPageToken).enqueue(new Callback<GoogleResult>() {
            @Override
            public void onResponse(Call<GoogleResult> call, Response<GoogleResult> response) {
                nextPageToken = response.body().getNextPageToken();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                callback.onResult(response.body().getResults(), null, nextPageToken);
            }

            @Override
            public void onFailure(Call<GoogleResult> call, Throwable t) {
                Log.d("-----", "Restaurant Data Source: onFailure");
            }
        });
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<String> params, @NonNull final LoadCallback<String, Result> callback) {
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<String> params, @NonNull final LoadCallback<String, Result> callback) {
        googleService.googleResultCall(location, params.key).enqueue(new Callback<GoogleResult>() {
            @Override
            public void onResponse(Call<GoogleResult> call, Response<GoogleResult> response) {
                if (response.body() != null) {
                    String key = response.body().getNextPageToken();

                    callback.onResult(response.body().getResults(), key);
                }
            }

            @Override
            public void onFailure(Call<GoogleResult> call, Throwable t) {
                Log.d("-----", "Restaurant Data Source: onFailure");
            }
        });
    }
}