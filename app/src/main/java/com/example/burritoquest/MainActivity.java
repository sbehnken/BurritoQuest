package com.example.burritoquest;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.burritoquest.Adapter.RestaurantListAdapter;
import com.example.burritoquest.Model.Result;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private RestaurantListAdapter mAdapter;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    LinearLayoutManager mLayoutManager;

    private RestaurantViewModel restaurantViewModel;
    private boolean isLoading = false;
    private String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        mAdapter = new RestaurantListAdapter(this);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        getLocationPermission();
    }

    private void getLocation() {
        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location l) {
                    if (l != null) {
                        location = (l.getLatitude() + "," + l.getLongitude());
                        RestaurantViewModelFactory factory = new RestaurantViewModelFactory(location);
                        restaurantViewModel = ViewModelProviders.of(MainActivity.this, factory).get(RestaurantViewModel.class);

                        if (!isLoading) {
                            isLoading = true;
                            restaurantViewModel.getResultLiveData().observe(MainActivity.this, new Observer<PagedList<Result>>() {
                                @Override
                                public void onChanged(@Nullable PagedList<Result> results) {
                                    isLoading = false;
                                    mAdapter.submitList(results);

                                }
                            });
                        } else {
                            Toast.makeText(MainActivity.this, "Unable to find current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        } catch (SecurityException e) {
            Log.e("-----", "getLocation: Security exception:" + e.getMessage());
        }
    }

    public void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            Toast.makeText(MainActivity.this, "You need locations permission enabled", Toast.LENGTH_SHORT).show();
        }
    }
}






