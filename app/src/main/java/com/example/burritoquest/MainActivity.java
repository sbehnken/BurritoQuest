package com.example.burritoquest;

import android.Manifest;
import android.app.ProgressDialog;
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
import com.example.burritoquest.Services.GoogleService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private RestaurantListAdapter mAdapter;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    LinearLayoutManager mLayoutManager;

    private String nextPageToken = null;
    private boolean noMoreResults = false;

    private String latlong = "";

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Wait while loading...");

        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new RestaurantListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if(!noMoreResults) {
                    getRestaurants(latlong);
                }
            }
        });

        getLocationPermission();
    }

    public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
        private int previousTotal = 0; // The total number of items in the dataset after the last load
        private boolean loading = true; // True if we are still waiting for the last set of data to load.
        private int visibleThreshold = 5; // The minimum amount of items to have below your current scroll position before loading more.
        int firstVisibleItem, visibleItemCount, totalItemCount;

        private int current_page = 1;

        private LinearLayoutManager mLinearLayoutManager;

        EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
            this.mLinearLayoutManager = linearLayoutManager;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            visibleItemCount = recyclerView.getChildCount();
            totalItemCount = mLinearLayoutManager.getItemCount();
            firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                    progressDialog.show();
                }
            }
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                current_page++;
                onLoadMore(current_page);
                loading = true;

            }
            progressDialog.dismiss();
        }
        public abstract void onLoadMore(int current_page);
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

                            latlong = currentLocation.getLatitude()  + "," + currentLocation.getLongitude();
                            getRestaurants(latlong);

                        } else {
                            Log.d("TAG", "onComplete: current location not found");
                            Toast.makeText(MainActivity.this, "Unable to find current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        } catch (SecurityException e) {
            Log.e( "TAG","getDeviceLocation: Security exception:" + e.getMessage());
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

    private void getRestaurants(final String location) {
        final GoogleService googleService = new GoogleService();
        //display loading notifcation loading bar
        googleService.googleResultCall(location, nextPageToken).enqueue(new Callback<GoogleResult>() {
            @Override
            public void onResponse(Call<GoogleResult> call, Response<GoogleResult> response) {

                    nextPageToken = response.body().getNextPageToken();
                    if(nextPageToken == null) {
                        noMoreResults = true;
                    }

                    mAdapter.setRestaurantList(response.body().getResults());
                    mAdapter.notifyDataSetChanged();
                    //hide loading notification bar

                    Log.d("TAG", "onGetRestaurants: Yay working!");
            }

            @Override
            public void onFailure(Call<GoogleResult> call, Throwable t) {
                Log.d("TAG", "onGetRestaurants: Not working yet");
            }
        });
    }
}






