package com.github.wojhan.epfuel;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.github.wojhan.epfuel.db.Car;
import com.github.wojhan.epfuel.db.FuelDatabase;
import com.github.wojhan.epfuel.db.Refuel;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;

public class RefuelListFragment extends Fragment {

    private RefuelListViewModel mViewModel;
    private FuelDatabase db;

    private Spinner mCarSpinner;

    private RecyclerView mRecyclerView;
    private RefuelListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Refuel> mRefuelList;

    public static RefuelListFragment newInstance() {
        return new RefuelListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.refuel_list_fragment, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RefuelListViewModel.class);
        // TODO: Use the ViewModel

        mRefuelList = new ArrayList<>();

        mRecyclerView = getView().findViewById(R.id.refuel_list_recyclerview);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RefuelListAdapter(mRefuelList);
        mRecyclerView.setAdapter(mAdapter);
        mCarSpinner = getView().findViewById(R.id.vehicle_spinner);

        db = Room.databaseBuilder(getContext(),
                FuelDatabase.class, "fuel").build();

        mCarSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    int carId = ((Car) parent.getSelectedItem()).getId();
                    db.fuelDao().getRefuels(carId).observe(RefuelListFragment.this, new Observer<List<Refuel>>() {

                        @Override
                        public void onChanged(@Nullable List<Refuel> refuels) {
                            if (!mRefuelList.isEmpty()) {
                                mRefuelList.clear();
                            }
                            mRefuelList.addAll(refuels);
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        db.carDao().getAll().observe(this, new Observer<List<Car>>() {
            @Override
            public void onChanged(@Nullable List<Car> cars) {
                mCarSpinner.setAdapter(new VehicleAdapter(cars));
                if (cars.size() > 0) {
                    mCarSpinner.setSelection(1);
                }
            }
        });
    }

}
