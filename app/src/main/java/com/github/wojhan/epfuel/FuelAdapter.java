package com.github.wojhan.epfuel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.wojhan.epfuel.db.Car;

import java.util.ArrayList;
import java.util.List;

public class FuelAdapter extends BaseAdapter {

    private List<String> mFuelTypeList;

    public FuelAdapter() {

    }

    public FuelAdapter(List<String> fuelTypeList) {
        mFuelTypeList = fuelTypeList;
    }

    @Override
    public int getCount() {
        return mFuelTypeList.size();
    }

    @Override
    public Object getItem(int position) {
        return mFuelTypeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        FuelHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fuel_type_spinner_item, parent, false);

            holder = new FuelHolder();
            holder.mName = convertView.findViewById(R.id.fuel_type_spinner_item_name);
            convertView.setTag(holder);
        } else {
            holder = (FuelHolder) convertView.getTag();
        }

        holder.mName.setText(mFuelTypeList.get(position));

        return convertView;
    }

    class FuelHolder {
        private TextView mName;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
//        View v = null;
//
//        // If this is the initial dummy entry, make it hidden
//        if (position == 0) {
//            TextView tv = new TextView(parent.getContext());
//            tv.setHeight(0);
//            tv.setVisibility(View.GONE);
//            v = tv;
//        } else {
//            // Pass convertView as null to prevent reuse of special case views
//            v = super.getDropDownView(position, null, parent);
//        }
//
//        // Hide scroll bar because it appears sometimes unnecessarily, this does not prevent scrolling
//        parent.setVerticalScrollBarEnabled(false);
//        return v;
        FuelHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fuel_type_spinner_dropdown_item, parent, false);

            holder = new FuelHolder();
            holder.mName = convertView.findViewById(R.id.fuel_type_spinner_item_name);
            convertView.setTag(holder);
        } else {
            holder = (FuelHolder) convertView.getTag();
        }

        holder.mName.setText(mFuelTypeList.get(position));

        return convertView;
    }
}
