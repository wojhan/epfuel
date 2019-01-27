package com.github.wojhan.epfuel;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.persistence.room.Room;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.wojhan.epfuel.db.Car;
import com.github.wojhan.epfuel.db.FuelDatabase;

import java.util.ArrayList;
import java.util.List;

public class UserCarsViewModel extends ViewModel {
    private FuelDatabase db;
    private List<Car> mCars = new ArrayList<>();

    public View view;

    public void loadCars(List<Car> cars) {
        if (!mCars.isEmpty()) {
            mCars.clear();
        }
        mCars.addAll(cars);
    }

    public List<Car> getCars() {
        return mCars;
    }
}
