package com.github.wojhan.epfuel;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.wojhan.epfuel.db.Car;
import com.github.wojhan.epfuel.db.FuelDatabase;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class UserCarsFragment extends Fragment {

    private UserCarsViewModel mViewModel;
    private FuelDatabase db;

    private RecyclerView mRecyclerView;
    private UserCarAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static UserCarsFragment newInstance() {
        return new UserCarsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_cars_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(UserCarsViewModel.class);
        mViewModel.view = getView();
        // TODO: Use the ViewModel

        mRecyclerView = getView().findViewById(R.id.user_cars_recyclerview);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        db = Room.databaseBuilder(getContext(),
                FuelDatabase.class, "fuel").build();

        db.carDao().getAll().observe(this, new Observer<List<Car>>() {

            @Override
            public void onChanged(@Nullable List<Car> cars) {
                mViewModel.loadCars(cars);

                mAdapter = new UserCarAdapter(mViewModel.getCars());
                mRecyclerView.setAdapter(mAdapter);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddCarActivity.class);
                startActivityForResult(intent, MainActivity.ADD_NEW_CAR_ACTIVITY);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MainActivity.ADD_NEW_CAR_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                String name = data.getExtras().getString("name");
                String make = data.getExtras().getString("make");
                String model = data.getExtras().getString("model");
                String description = data.getExtras().getString("description");
                String firstTank = data.getExtras().getString("firstTank");
                String secondTank = null;
                if (data.getExtras().get("secondTank") != null) {
                    secondTank = (String) data.getExtras().getString("secondTank");
                }

                final Car car = new Car();
                car.setName(name);
                car.setMake(make);
                car.setModel(model);
                car.setDescription(description);
                car.setFirstTank(firstTank);
                car.setSecondTank(secondTank);
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        db.carDao().insert(car);
                    }
                });
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
