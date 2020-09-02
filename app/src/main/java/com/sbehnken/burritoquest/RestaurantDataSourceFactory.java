package com.sbehnken.burritoquest;


import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.PageKeyedDataSource;

import com.sbehnken.burritoquest.Model.Result;


public class RestaurantDataSourceFactory extends DataSource.Factory {
    private MutableLiveData<PageKeyedDataSource<String, Result>>  resultLiveDataSource = new MutableLiveData<>();
    private RestaurantDataSource restaurantDataSource;

    public RestaurantDataSourceFactory(RestaurantDataSource restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    @Override
    public DataSource create() {
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
