package com.example.burritoquest;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class RestaurantViewModelFactory implements ViewModelProvider.Factory {
    private String location;

    public RestaurantViewModelFactory(String location) {
        this.location = location;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RestaurantViewModel(location);
    }
}
