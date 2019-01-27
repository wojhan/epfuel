package com.github.wojhan.epfuel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.wojhan.epfuel.db.Car;

import java.util.List;

public class VehicleAdapter extends BaseAdapter {

    private List<Car> mVehicleList;

    public VehicleAdapter() {

    }

    public VehicleAdapter(List<Car> vehicleList) {
        mVehicleList = vehicleList;
    }

    @Override
    public int getCount() {
        return mVehicleList.size();
    }

    @Override
    public Object getItem(int position) {
        return mVehicleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        VehicleHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_vehicle_spinner, parent, false);

            holder = new VehicleHolder();
            holder.mName = convertView.findViewById(R.id.vehicle_spinner_name);
            convertView.setTag(holder);
        } else {
            holder = (VehicleHolder)convertView.getTag();
        }

        Car vehicle = mVehicleList.get(position);
        holder.mName.setText(vehicle.getName());

        return convertView;
    }

    class VehicleHolder {
        private TextView mName;
    }
}
