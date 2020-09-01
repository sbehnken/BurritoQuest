package com.example.burritoquest;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.example.burritoquest.Model.Result;

import java.util.concurrent.Executor;


public class RestaurantViewModel extends ViewModel {
    private Executor mExecutor;
    private static final int PAGE_SIZE = 20;
    private LiveData<PagedList<Result>> resultLiveData;
    private LiveData<RestaurantDataSource> liveDataSource;
    private RestaurantDataSourceFactory restaurantDataSourceFactory;
    private String location;

    public RestaurantViewModel(String loc) {
        location = loc;
        RestaurantDataSource restaurantDataSource = new RestaurantDataSource(loc);
        liveDataSource = new MutableLiveData<>();
        ((MutableLiveData<RestaurantDataSource>) liveDataSource).setValue(restaurantDataSource);
        restaurantDataSourceFactory = new RestaurantDataSourceFactory(liveDataSource.getValue());

//         mExecutor = Executors.newFixedThreadPool(1);

         PagedList.Config config = (new PagedList.Config.Builder())
                 .setEnablePlaceholders(false)
                 .setPageSize(PAGE_SIZE)
                 .build();

         resultLiveData = (new LivePagedListBuilder(restaurantDataSourceFactory, config))
//                 .setFetchExecutor(mExecutor)
                 .build();

    }

    public LiveData<PagedList<Result>> getResultLiveData() {
        return resultLiveData;
    }

    public String getLocation() {
        return location;
    }
}
