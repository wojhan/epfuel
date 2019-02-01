package com.github.wojhan.epfuel;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class RecentFuelAdapter extends RecyclerView.Adapter<RecentFuelAdapter.RecentFuelViewHolder> {

    private List<RecentRefuel> mRecentFuelList;

    public RecentFuelAdapter(List<RecentRefuel> refuelList) {
        mRecentFuelList = refuelList;
    }

    @NonNull
    @Override
    public RecentFuelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recent_fuel_item, viewGroup, false);
        RecentFuelViewHolder rvh = new RecentFuelViewHolder(v);
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecentFuelViewHolder holder, int position) {
        RecentRefuel currentItem = mRecentFuelList.get(position);

        holder.mLastPriceValue.setText(DecimalFormat.getCurrencyInstance().format((double) currentItem.getLastPrice()));
        holder.mLastConsumptionValue.setText(String.format("%.2f l", currentItem.getLastConsumption()));
        holder.mAvgConsumptionValue.setText(String.format("%.2f l", currentItem.getAvgConsumption()));
        holder.mDate.setText(String.valueOf(DateFormat.getDateInstance(DateFormat.SHORT).format(currentItem.getDate())));
        holder.mType.setText(currentItem.getType());
    }

    @Override
    public int getItemCount() {
        return mRecentFuelList.size();
    }

    public static class RecentFuelViewHolder extends RecyclerView.ViewHolder {
        public TextView mAvgConsumptionValue, mAvgConsumptionLabel;
        public TextView mLastConsumptionValue, mLastConsmumptionLabel;
        public TextView mLastPriceValue, mLastPriceLabel;
        public TextView mDate;
        public TextView mType;

        public RecentFuelViewHolder(@NonNull View itemView) {
            super(itemView);
            mAvgConsumptionLabel = itemView.findViewById(R.id.recent_fuel_item_avg_consumption_label);
            mAvgConsumptionValue = itemView.findViewById(R.id.recent_fuel_item_avg_consumption_value);
            mLastConsmumptionLabel = itemView.findViewById(R.id.recent_fuel_item_last_consumption_label);
            mLastConsumptionValue = itemView.findViewById(R.id.recent_fuel_item_last_consumption_value);
            mLastPriceLabel = itemView.findViewById(R.id.recent_fuel_item_last_price_label);
            mLastPriceValue = itemView.findViewById(R.id.recent_fuel_item_last_price_value);
            mDate = itemView.findViewById(R.id.recent_fuel_item_date);
            mType = itemView.findViewById(R.id.recent_fuel_item_label);
        }
    }
}
