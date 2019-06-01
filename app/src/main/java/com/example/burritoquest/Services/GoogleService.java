package com.example.burritoquest.Services;


import com.example.burritoquest.BuildConfig;
import com.example.burritoquest.Model.GoogleResult;
import com.example.burritoquest.Model.Location;
import com.google.android.gms.maps.model.Marker;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoogleService {

    private GoogleInterface googleInterface;

    public GoogleService() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        googleInterface = retrofit.create(GoogleInterface.class);

    }

    public Call<GoogleResult> googleResultCall(String location) {
        return googleInterface.googleResultCall( "CrQCKgEAAKjj2wJzezxjp--LUx6xdY3c7WWj685b0f-w7SX-6hPzBUZFtVXB87wSwOsIV71XBkgmPUTbqnrQg88L8HgIQ2CDjOhZr54sDgALZhcW07Or02wNJWvWkfyffvzHUjTkjKJbV28SbsioFIToxGgZZXkDuebXYWwhLH7a6FAV_-gtPoz07JIkUk-_LnpkEuSU75t8cYvmLL77qxG5j7JRaNIetu4tPiucsdxsxQdvieRgcb94NW-pcq63i-8KweDo-tt9a9oyheXAgIDRPhrLdqNJSzbOjBI61iUNF8lRd6ISReOG9sq4wWETfqqlcRqq1786I6YgFJ_IaEj0Y0U8nhHDurYw4qNWz0tsaZgr8uD4HNjvnD1pKOnAJGg_yZzKJ-BrVwLmCmLx54wku1VFLU0SELrvWisozLDFkqfJQtCDk1caFLmbjWSQkZl8rr5NHKSdzq98iP-C", 2000, "restaurant", "burrito", BuildConfig.ApiKey, location);
    }
    }


