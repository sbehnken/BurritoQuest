package com.example.burritoquest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.burritoquest.Adapter.RestaurantListAdapter;
import com.example.burritoquest.Model.GoogleResult;
import com.example.burritoquest.Model.Result;
import com.example.burritoquest.Services.GoogleService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private RestaurantListAdapter mAdapter;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);

        mRecyclerView.setHasFixedSize(true);
        mAdapter = new RestaurantListAdapter(this, new ArrayList<RestaurantItem>());
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        getLocationPermission();
    }

    private void getDeviceLocation() {
        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful() && task.getResult() != null) {
                            Log.d("TAG", "onComplete: Found location!");
                            android.location.Location currentLocation = (Location) task.getResult();

                            getRestaurants((currentLocation.getLatitude())  + "," +
                                    (currentLocation.getLongitude()));
                        } else {
                            Log.d("TAG", "onComplete: current location not found");
                            Toast.makeText(MainActivity.this, "unable to find current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        } catch (SecurityException e) {
            Log.e( "TAG","get Device Location: Security exception:" + e.getMessage());
        }
    }

    public void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                getDeviceLocation();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getDeviceLocation();
        } else {
            Toast.makeText(MainActivity.this, "You need locations permission enabled", Toast.LENGTH_SHORT).show();
        }
    }

    private void getRestaurants(String location) {
        final GoogleService googleService = new GoogleService();
        googleService.googleResultCall().enqueue(new Callback<GoogleResult>() {
            @Override
            public void onResponse(Call<GoogleResult> call, Response<GoogleResult> response) {
                List<RestaurantItem> restaurantList = new ArrayList<>();
                List<Result> results = response.body().getResults();
                for(int i = 0; i < results.size(); i++) {

                    String priceLevel = (results.get(i).getPriceLevel() != null) ? Integer.toString(results.get(i).getPriceLevel()) : "N/A";
                    RestaurantItem restaurantItem = new RestaurantItem(results.get(i).getName(), results.get(i).getVicinity(),
                            priceLevel, results.get(i).getRating(), results.get(i).getGeometry().getLocation());

                    restaurantList.add(restaurantItem);
                }
                mAdapter.setRestaurantList(restaurantList);
                mAdapter.notifyDataSetChanged();
                Log.d("TAG", "Yay working!");
            }

            @Override
            public void onFailure(Call<GoogleResult> call, Throwable t) {
                Log.d("TAG", "No worky yet");
            }
        });
    }
}






