package com.github.wojhan.epfuel;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.wojhan.epfuel.db.Refuel;

import org.w3c.dom.Text;

import java.sql.Ref;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class RefuelListAdapter extends RecyclerView.Adapter<RefuelListAdapter.RefuelListViewHolder> {

    private List<Refuel> mRefuelList;

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

    public RefuelListAdapter(List<Refuel> refuelList) {
        mRefuelList = refuelList;
    }

    @NonNull
    @Override
    public RefuelListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.refuel_list_item, viewGroup, false);
        RefuelListViewHolder rvh = new RefuelListViewHolder(v, mListener, mLongClickListener);
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RefuelListViewHolder holder, int position) {
        int additionalKilometers = 0;
        Refuel currentItem = mRefuelList.get(position);

        if (position + 1 < mRefuelList.size()) {
            Refuel previousItem = mRefuelList.get(position + 1);
            additionalKilometers = currentItem.getCounter() - previousItem.getCounter();
        }

        holder.mDetails.setText("Pierwsze tankowanie");

        if (additionalKilometers > 0) {
            float consumption = currentItem.getAmount() / additionalKilometers * 100;
            holder.mAdditionalKilometers.setText("+" + additionalKilometers + " km");
            holder.mDetails.setText(String.format("l/100 km: %.2f", consumption));
        }

        holder.mDate.setText(String.valueOf(DateFormat.getDateInstance(DateFormat.LONG).format(currentItem.getDate())));
        holder.mFuelDetails.setText(String.format("%.2f l, %s", currentItem.getAmount(), DecimalFormat.getCurrencyInstance(Locale.getDefault()).format((double) currentItem.getPriceForLiter())));
        holder.mPrice.setText(DecimalFormat.getCurrencyInstance(Locale.getDefault()).format((double) (currentItem.getAmount() * currentItem.getPriceForLiter())));
        holder.mCounter.setText(currentItem.getCounter() + " km");
    }

    @Override
    public int getItemCount() {
        return mRefuelList.size();
    }

    public static class RefuelListViewHolder extends RecyclerView.ViewHolder {
        public TextView mDate;
        public TextView mPrice;
        public TextView mCounter;
        public TextView mAdditionalKilometers;
        public TextView mFuelDetails;
        public TextView mDetails;


        public RefuelListViewHolder(@NonNull View itemView, final OnCreateContextMenuListener mListener, final OnLongClickListener mLongClickListener) {
            super(itemView);

            mDate = itemView.findViewById(R.id.refuel_list_item_date);
            mPrice = itemView.findViewById(R.id.refuel_list_item_price);
            mCounter = itemView.findViewById(R.id.refuel_list_item_counter);
            mAdditionalKilometers = itemView.findViewById(R.id.refuel_list_item_additional_kilometers);
            mFuelDetails = itemView.findViewById(R.id.refuel_list_item_fuel_details);
            mDetails = itemView.findViewById(R.id.refuel_list_item_details);

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
