package com.example.burritoquest;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PageKeyedDataSource;
import android.arch.paging.PagedList;

import com.example.burritoquest.Model.Result;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class RestaurantViewModel extends ViewModel {
    private Executor mExecutor;
    private static final int PAGE_SIZE = 20;
    private LiveData<PagedList<Result>> resultLiveData;
    private LiveData<PageKeyedDataSource<String, Result>> liveDataSource;
    private RestaurantDataSourceFactory restaurantDataSourceFactory;

    public RestaurantViewModel() {
         restaurantDataSourceFactory = new RestaurantDataSourceFactory();
         liveDataSource = restaurantDataSourceFactory.getResultLiveDataSource();

         mExecutor = Executors.newFixedThreadPool(5);

         PagedList.Config config = (new PagedList.Config.Builder())
                 .setEnablePlaceholders(false)
                 .setPageSize(PAGE_SIZE)
                 .build();

         resultLiveData = (new LivePagedListBuilder(restaurantDataSourceFactory, config))
                 .setFetchExecutor(mExecutor)
                 .build();

    }

    public LiveData<PagedList<Result>> getResultLiveData() {
        return resultLiveData;
    }

    public LiveData<PageKeyedDataSource<String, Result>> getLiveDataSource() {
        return liveDataSource;
    }

    public RestaurantDataSourceFactory getRestaurantDataSourceFactory() {
        return restaurantDataSourceFactory;
    }
}
