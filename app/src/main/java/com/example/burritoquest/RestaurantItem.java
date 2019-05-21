package com.example.burritoquest;


import com.example.burritoquest.Model.Location;

public class RestaurantItem {
    private String mRestaurantName;
    private String mRestaurantAddress;
    private String mPriceRating;
    private Double mUserRating;
    private Location mLocation;

    public RestaurantItem(String restaurantName, String restaurantAddress, String priceRating, Double userRating, Location location) {
        mRestaurantName = restaurantName;
        mRestaurantAddress = restaurantAddress;
        mPriceRating = priceRating;
        mUserRating = userRating;
        mLocation = location;

    }

    public String getRestaurantName() {
        return mRestaurantName;
    }

    public String getRestaurantAddress() {
        return mRestaurantAddress;
    }

    public String getPriceRating() {
        return mPriceRating;
    }

    public String getDollarSignRating() {
        if (mPriceRating.equals("1")) {
            return "$";
        }
        if (mPriceRating.equals("2")) {
            return "$$";
        }
        if (mPriceRating.equals("3")) {
            return "$$$";
        }
        if (mPriceRating.equals("4")) {
            return "$$$$";
        } else {
            return "N/A";
        }
    }

    public Double getUserRating() {
        return mUserRating;
    }

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location mLocation) {
        this.mLocation = mLocation;
    }
}
