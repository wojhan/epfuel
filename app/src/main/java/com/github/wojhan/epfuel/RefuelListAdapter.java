package com.github.wojhan.epfuel;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.wojhan.epfuel.db.Refuel;

import org.w3c.dom.Text;

import java.sql.Ref;
import java.util.HashMap;
import java.util.List;

public class RefuelListAdapter extends RecyclerView.Adapter<RefuelListAdapter.RefuelListViewHolder> {

    private List<Refuel> mRefuelList;

    public RefuelListAdapter(List<Refuel> refuelList) {
        mRefuelList = refuelList;
    }

    @NonNull
    @Override
    public RefuelListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.refuel_list_item, viewGroup, false);
        RefuelListViewHolder rvh = new RefuelListViewHolder(v);
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


        holder.mDate.setText(currentItem.getDate());
        holder.mDetails.setText("l/100 km: nie umiem policzyć");
        holder.mFuelDetails.setText("19,14 l -> 2,31 zł/l (LPG)");
        holder.mAdditionalKilometers.setText("+" + additionalKilometers + " km");
        holder.mPrice.setText(String.format("%.2f", currentItem.getAmount() * currentItem.getPriceForLiter()));
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


        public RefuelListViewHolder(@NonNull View itemView) {
            super(itemView);

            mDate = itemView.findViewById(R.id.refuel_list_item_date);
            mPrice = itemView.findViewById(R.id.refuel_list_item_price);
            mCounter = itemView.findViewById(R.id.refuel_list_item_counter);
            mAdditionalKilometers = itemView.findViewById(R.id.refuel_list_item_additional_kilometers);
            mFuelDetails = itemView.findViewById(R.id.refuel_list_item_fuel_details);
            mDetails = itemView.findViewById(R.id.refuel_list_item_details);
        }
    }
}
