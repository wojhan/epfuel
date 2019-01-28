package com.github.wojhan.epfuel;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.wojhan.epfuel.db.Car;
import com.github.wojhan.epfuel.db.FuelDatabase;

import java.util.List;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;

    private FuelDatabase db;

    private SharedPreferences preferences;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mViewModel.view = getView();

        mViewModel.getInformation();

        preferences = getActivity().getSharedPreferences("epfuel", Context.MODE_PRIVATE);

        db = Room.databaseBuilder(getContext(),
                FuelDatabase.class, "fuel").build();

        db.carDao().getAll().observe(this, new Observer<List<Car>>() {

            @Override
            public void onChanged(@Nullable List<Car> cars) {
                mViewModel.getCars(cars, preferences);
            }
        });
        // TODO: Use the ViewModel
    }

}
