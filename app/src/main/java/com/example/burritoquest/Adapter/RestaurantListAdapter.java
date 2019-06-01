package com.example.burritoquest.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.burritoquest.MapActivity;
import com.example.burritoquest.R;
import com.example.burritoquest.RestaurantItem;

import java.util.List;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.RestaurantViewHolder> {
    private Context mContext;
    private List<RestaurantItem> mRestaurantList;

    public RestaurantListAdapter(Context context, List<RestaurantItem> restaurantList) {
        mContext = context;
        mRestaurantList = restaurantList;
    }

    public void setRestaurantList(List<RestaurantItem> mRestaurantList) {
        this.mRestaurantList = mRestaurantList;
    }

    @NonNull
    @Override
    public RestaurantListAdapter.RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.restaurant_item, viewGroup, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantListAdapter.RestaurantViewHolder holder, int i) {
        final RestaurantItem currentItem = mRestaurantList.get(i);

        String restaurantName = currentItem.getRestaurantName();
        String restaurantAddress = currentItem.getRestaurantAddress();
        String priceRating = "Price: " + currentItem.getDollarSignRating();
        String userRating = "User Rating: " + currentItem.getUserRating().toString();

        holder.mRestaurantName.setText(restaurantName);
        holder.mRestaurantAddress.setText(restaurantAddress);
        holder.mPriceRating.setText(priceRating);
        holder.mUserRating.setText(userRating);
        holder.mForwardArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MapActivity.class);
                intent.putExtra("name", currentItem.getRestaurantName());
                intent.putExtra("address", currentItem.getRestaurantAddress());
                intent.putExtra("latitude", currentItem.getLocation().getLat());
                intent.putExtra("longitude", currentItem.getLocation().getLng());
                intent.putExtra("price_level", currentItem.getDollarSignRating());
                intent.putExtra("rating", currentItem.getUserRating().toString());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRestaurantList.size();
    }

    public class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mRestaurantName;
        TextView mRestaurantAddress;
        TextView mPriceRating;
        TextView mUserRating;
        ImageButton mForwardArrow;

        RestaurantViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            mRestaurantName = itemView.findViewById(R.id.restaurant_name);
            mRestaurantAddress = itemView.findViewById(R.id.restaurant_address);
            mPriceRating = itemView.findViewById(R.id.price_rating);
            mUserRating = itemView.findViewById(R.id.user_rating);
            mForwardArrow = itemView.findViewById(R.id.arrow_forward);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int itemPosition = getLayoutPosition();
            final RestaurantItem currentItem = mRestaurantList.get(itemPosition);

            Intent intent = new Intent(mContext, MapActivity.class);
            intent.putExtra("name", currentItem.getRestaurantName());
            intent.putExtra("address", currentItem.getRestaurantAddress());
            intent.putExtra("latitude", currentItem.getLocation().getLat());
            intent.putExtra("longitude", currentItem.getLocation().getLng());
            intent.putExtra("price_level", currentItem.getDollarSignRating());
            intent.putExtra("rating", currentItem.getUserRating().toString());
            mContext.startActivity(intent);
        }
    }
}
