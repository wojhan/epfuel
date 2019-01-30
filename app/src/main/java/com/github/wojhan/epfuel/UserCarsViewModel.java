package com.github.wojhan.epfuel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.persistence.room.Room;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.wojhan.epfuel.db.Car;
import com.github.wojhan.epfuel.db.CarRepository;
import com.github.wojhan.epfuel.db.FuelDatabase;
import com.github.wojhan.epfuel.db.FuelRepository;

import java.util.ArrayList;
import java.util.List;

public class UserCarsViewModel extends AndroidViewModel {
    private FuelDatabase db;
    private CarRepository carRepository;
    private LiveData<List<Car>> carListLiveData;

    public UserCarsViewModel(Application application) {
        super(application);
        db = FuelDatabase.getDatabase(application);

        carRepository = new CarRepository(application);
        carListLiveData = carRepository.getAllCars();
    }

    public LiveData<List<Car>> getAllCars() {
        return carListLiveData;
    }
}
