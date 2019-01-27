package com.github.wojhan.epfuel;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.wojhan.epfuel.db.Car;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

public class UserCarAdapter extends RecyclerView.Adapter<UserCarAdapter.UserCarViewHolder> {

    private List<Car> mUserCarList;

    public UserCarAdapter(List<Car> userCarList) {
        mUserCarList = userCarList;
    }

    @NonNull
    @Override
    public UserCarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_car_item, viewGroup, false);
        UserCarViewHolder rvh = new UserCarViewHolder(v);
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull UserCarViewHolder holder, int position) {
        Car currentItem = mUserCarList.get(position);

        holder.mModelTextView.setText(currentItem.getModel());
        holder.mMakeTextView.setText(currentItem.getMake());
        holder.mNameTextView.setText(currentItem.getName());
//        holder.mImageView
    }

    @Override
    public int getItemCount() {
        return mUserCarList.size();
    }

    public static class UserCarViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mNameTextView;
        public TextView mMakeTextView;
        public TextView mModelTextView;

        public UserCarViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.user_car_item_image);
            mNameTextView = itemView.findViewById(R.id.user_car_item_name);
            mMakeTextView = itemView.findViewById(R.id.user_car_item_make);
            mModelTextView = itemView.findViewById(R.id.user_car_item_model);

        }
    }
}
