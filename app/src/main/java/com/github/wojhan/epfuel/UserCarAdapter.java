package com.github.wojhan.epfuel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.wojhan.epfuel.db.Car;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

public class UserCarAdapter extends RecyclerView.Adapter<UserCarAdapter.UserCarViewHolder> {

    private List<Car> mUserCarList;
    private OnCreateContextMenuListener mListener;
    private OnLongClickListener mLongClickListener;

    interface OnCreateContextMenuListener {
        void onCreateContextMenu(ContextMenu menu, View v);
    }

    interface OnLongClickListener {
        void onLongClick(int position);
    }

    public void setOnLongClickListener(OnLongClickListener listener) {
        mLongClickListener = listener;
    }


    public void setOnCreateContextMenuListener(OnCreateContextMenuListener listener) {
        mListener = listener;
    }

    public UserCarAdapter(List<Car> userCarList) {
        mUserCarList = userCarList;
    }

    @NonNull
    @Override
    public UserCarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_car_item, viewGroup, false);
        UserCarViewHolder rvh = new UserCarViewHolder(v, mListener, mLongClickListener);
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull UserCarViewHolder holder, int position) {
        Car currentItem = mUserCarList.get(position);

        holder.mModelTextView.setText(currentItem.getModel());
        holder.mMakeTextView.setText(currentItem.getMake());
        holder.mNameTextView.setText(currentItem.getName());

        if (currentItem.getImage() != null && !currentItem.getImage().isEmpty()) {
            try {
                File f = new File(currentItem.getImage());
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                holder.mImageView.setImageBitmap(b);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


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

        public UserCarViewHolder(@NonNull View itemView, final OnCreateContextMenuListener mListener, final OnLongClickListener mLongClickListener) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.user_car_item_image);
            mNameTextView = itemView.findViewById(R.id.user_car_item_name);
            mMakeTextView = itemView.findViewById(R.id.user_car_item_make);
            mModelTextView = itemView.findViewById(R.id.user_car_item_model);
            itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    if (mListener != null) {
                        mListener.onCreateContextMenu(menu, v);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mLongClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mLongClickListener.onLongClick(position);
                        }
                    }
                    return false;
                }
            });
        }


    }
}
