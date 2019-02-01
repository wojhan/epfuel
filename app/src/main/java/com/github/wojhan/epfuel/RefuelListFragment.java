package com.github.wojhan.epfuel;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.github.wojhan.epfuel.db.Car;
import com.github.wojhan.epfuel.db.FuelDatabase;
import com.github.wojhan.epfuel.db.Refuel;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class RefuelListFragment extends Fragment {

    private RefuelListViewModel mViewModel;
    private FuelDatabase db;

    private Spinner mCarSpinner;

    private RecyclerView mRecyclerView;
    private RefuelListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Refuel> mRefuelList;
    private List<Car> mCarList;

    private SharedPreferences preferences;
    private int mCurrentPos;

    public static RefuelListFragment newInstance() {
        return new RefuelListFragment();
    }

    Observer<List<Car>> carObserver = new Observer<List<Car>>() {
        @Override
        public void onChanged(@Nullable List<Car> cars) {
            if (!mCarList.isEmpty()) {
                mCarList.clear();
            }
            mCarList.addAll(cars);
            mCarSpinner.setAdapter(new VehicleAdapter(cars));
            mCarSpinner.setSelection(preferences.getInt("chosenCarPosition", 0));
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.refuel_list_fragment, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RefuelListViewModel.class);

        preferences = getActivity().getSharedPreferences("epfuel", Context.MODE_PRIVATE);

        mRefuelList = new ArrayList<>();
        mCarList = new ArrayList<>();

        mRecyclerView = getView().findViewById(R.id.refuel_list_recyclerview);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RefuelListAdapter(mRefuelList);
        mRecyclerView.setAdapter(mAdapter);
        mCarSpinner = getView().findViewById(R.id.vehicle_spinner);

        mViewModel.getCars().observe(this, carObserver);

        db = FuelDatabase.getDatabase(getContext());

        mCarSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    int carId = ((Car) parent.getSelectedItem()).getId();
                    mViewModel.getRefuels(carId).observe(RefuelListFragment.this, new Observer<List<Refuel>>() {
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

        mAdapter.setOnCreateContextMenuListener(new RefuelListAdapter.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v) {
                MenuInflater menuInflater = getActivity().getMenuInflater();
                menuInflater.inflate(R.menu.delete_context_menu, menu);
            }
        });

        mAdapter.setOnLongClickListener(new RefuelListAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(int position) {
                mCurrentPos = position;
            }
        });

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = -1;
        try {
            position = mCurrentPos;
        } catch (Exception e) {
            Log.d("epfuelapp", e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()) {
            case R.id.context_menu_remove:
                //mViewModel.deleteCar(mCarList.get(position));
                final int finalPosition = position;
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        db.fuelDao().delete(mRefuelList.get(finalPosition));
                        mAdapter.notifyItemRemoved(finalPosition);
                    }
                });
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MainActivity.EDIT_REFUEL_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                int carId = data.getExtras().getInt("carId");
                int counter = data.getExtras().getInt("counter");
                float fuelAmount = data.getExtras().getFloat("fuelAmount");
                float priceForLiter = data.getExtras().getFloat("priceForLiter");
                long date = data.getExtras().getLong("date");
                String fuelType = data.getExtras().getString("fuelType");
                int fuel = data.getExtras().getInt("fuel");

                Date dateToSave = new Date();
                dateToSave.setTime(date);

                final Refuel refuel = mRefuelList.get(mCurrentPos);
                refuel.setAmount(fuelAmount);
                refuel.setCarId(carId);
                refuel.setCounter(counter);
                refuel.setDate(dateToSave);
                refuel.setPriceForLiter(priceForLiter);
                refuel.setType(fuelType);
                refuel.setFuel(fuel);

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        db.fuelDao().update(refuel);
                    }
                });
            }
        }

        //super.onActivityResult(requestCode, resultCode, data);
    }
}
