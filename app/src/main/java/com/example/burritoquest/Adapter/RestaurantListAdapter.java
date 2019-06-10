package com.example.burritoquest.Adapter;

import android.annotation.SuppressLint;
import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.AsyncDifferConfig;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.burritoquest.MapActivity;
import com.example.burritoquest.Model.GoogleResult;
import com.example.burritoquest.Model.Result;
import com.example.burritoquest.R;
import com.example.burritoquest.RestaurantItem;

import java.util.ArrayList;
import java.util.List;

public class RestaurantListAdapter extends PagedListAdapter<Result, RestaurantListAdapter.RestaurantViewHolder> {
    private Context mContext;

    public RestaurantListAdapter(Context context) {
        super(DIFF_CALLBACK);
        mContext = context;
    }

    @NonNull
    @Override
    public RestaurantListAdapter.RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.restaurant_item, viewGroup, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantListAdapter.RestaurantViewHolder holder, int position) {
        Result currentItem = getItem(position);

        String restaurantName = currentItem.getName();
        String restaurantAddress = currentItem.getVicinity();
        String priceRating = "Price Level: " + currentItem.getPriceLevel();
        String userRating = "User Rating: " + currentItem.getRating().toString();

        holder.mRestaurantName.setText(restaurantName);
        holder.mRestaurantAddress.setText(restaurantAddress);
        holder.mPriceRating.setText(priceRating);
        holder.mUserRating.setText(userRating);
    }

    public class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mRestaurantName;
        TextView mRestaurantAddress;
        TextView mPriceRating;
        TextView mUserRating;
        ImageView mForwardArrow;

        RestaurantViewHolder(View itemView) {
            super(itemView);
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
            final Result currentItem = getItem(itemPosition);

                Intent intent = new Intent(mContext, MapActivity.class);
                intent.putExtra("name", currentItem.getName());
                intent.putExtra("address", currentItem.getVicinity());
                intent.putExtra("latitude", currentItem.getGeometry().getLocation().getLat());
                intent.putExtra("longitude", currentItem.getGeometry().getLocation().getLng());
                intent.putExtra("price_level", "Price level: " + currentItem.getPriceLevel().toString());
                intent.putExtra("rating", "User rating: " + currentItem.getRating().toString());
                mContext.startActivity(intent);
            }
        }


    private static DiffUtil.ItemCallback<Result> DIFF_CALLBACK = new DiffUtil.ItemCallback<Result>() {
        @Override
        public boolean areItemsTheSame(@NonNull Result oldItem, @NonNull Result newItem) {
            return oldItem.getName().equals(newItem.getName());
        }

        @Override
        @SuppressLint("DiffUtilEquals")
        public boolean areContentsTheSame(@NonNull Result oldItem, @NonNull Result newItem) {
            return oldItem == newItem;
        }
    };
}
