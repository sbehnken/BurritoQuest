package com.example.burritoquest;


import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.PageKeyedDataSource;

import com.example.burritoquest.Model.GoogleResult;
import com.example.burritoquest.Model.Result;


public class RestaurantDataSourceFactory extends DataSource.Factory {
    private MutableLiveData<PageKeyedDataSource<String, Result>>  resultLiveDataSource = new MutableLiveData<>();
    private RestaurantDataSource restaurantDataSource;

    @Override
    public DataSource create() {
        restaurantDataSource = new RestaurantDataSource();

        resultLiveDataSource.postValue(restaurantDataSource);
        return restaurantDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<String, Result>> getResultLiveDataSource() {
        return resultLiveDataSource;
    }

    public RestaurantDataSource getRestaurantDataSource() {
        return restaurantDataSource;
    }
}
