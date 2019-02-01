package com.github.wojhan.epfuel;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.wojhan.epfuel.db.Refuel;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class RecentRefuelsAdapter extends RecyclerView.Adapter<RecentRefuelsAdapter.RecentRefuelsViewHolder> {

    private List<Refuel> mRecentRefuelList;

    public RecentRefuelsAdapter(List<Refuel> refuelList) {
        mRecentRefuelList = refuelList;
    }

    @NonNull
    @Override
    public RecentRefuelsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recent_refuels_item, viewGroup, false);
        RecentRefuelsViewHolder rvh = new RecentRefuelsViewHolder(v);
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecentRefuelsViewHolder holder, int position) {
        Refuel currentItem = mRecentRefuelList.get(position);

        float price = currentItem.getAmount() * currentItem.getPriceForLiter();

        holder.mDate.setText(String.valueOf(DateFormat.getDateInstance(DateFormat.SHORT).format(currentItem.getDate())));
        holder.mAmount.setText(String.format("%.2f l", currentItem.getAmount()));
        holder.mPrice.setText(DecimalFormat.getCurrencyInstance(Locale.getDefault()).format((double) price));
    }

    @Override
    public int getItemCount() {
        return mRecentRefuelList.size();
    }

    public static class RecentRefuelsViewHolder extends RecyclerView.ViewHolder {
        public TextView mPrice;
        public TextView mDate;
        public TextView mAmount;

        public RecentRefuelsViewHolder(@NonNull View itemView) {
            super(itemView);
            mPrice = itemView.findViewById(R.id.recent_refuels_item_price);
            mDate = itemView.findViewById(R.id.recent_refuels_item_date);
            mAmount = itemView.findViewById(R.id.recent_refuels_item_amount);
        }
    }
}
