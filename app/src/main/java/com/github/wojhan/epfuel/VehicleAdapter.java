package com.github.wojhan.epfuel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.wojhan.epfuel.db.Car;

import java.util.ArrayList;
import java.util.List;

public class VehicleAdapter extends BaseAdapter {

    private List<Object> mVehicleList;

    public VehicleAdapter() {

    }

    public VehicleAdapter(List<Car> vehicleList) {
        mVehicleList = new ArrayList<Object>(vehicleList);
        mVehicleList.add(0, "Wybierz pojazd");
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
        if (mVehicleList.get(position) == Car.class) {
            return ((Car) mVehicleList.get(position)).getId();
        }
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        VehicleHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_vehicle_spinner, parent, false);

            holder = new VehicleHolder();
            holder.mName = convertView.findViewById(R.id.vehicle_spinner_name);
            holder.mModel = convertView.findViewById(R.id.vehicle_spinner_model);
            convertView.setTag(holder);
        } else {
            holder = (VehicleHolder)convertView.getTag();
        }

        if (position > 0) {
            Car vehicle = (Car) mVehicleList.get(position);
            holder.mName.setText(vehicle.getName());
            holder.mModel.setText(vehicle.getMake() + " " + vehicle.getModel());
        } else {
            holder.mName.setText("Pojazd");
            holder.mModel.setText("Wybierz pojazd");

        }
        return convertView;
    }

    class VehicleHolder {
        private TextView mName;
        private TextView mModel;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View v = null;

        // If this is the initial dummy entry, make it hidden
        if (position == 0) {
            TextView tv = new TextView(parent.getContext());
            tv.setHeight(0);
            tv.setVisibility(View.GONE);
            v = tv;
        } else {
            // Pass convertView as null to prevent reuse of special case views
            v = super.getDropDownView(position, null, parent);
        }

        // Hide scroll bar because it appears sometimes unnecessarily, this does not prevent scrolling
        parent.setVerticalScrollBarEnabled(false);
        return v;
    }
}
