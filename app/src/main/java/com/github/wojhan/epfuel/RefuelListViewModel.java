package com.github.wojhan.epfuel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.github.wojhan.epfuel.db.Car;
import com.github.wojhan.epfuel.db.CarRepository;
import com.github.wojhan.epfuel.db.FuelDatabase;
import com.github.wojhan.epfuel.db.FuelRepository;
import com.github.wojhan.epfuel.db.Refuel;

import java.util.List;

public class RefuelListViewModel extends AndroidViewModel {

    private FuelDatabase db;
    private CarRepository carRepository;
    private FuelRepository fuelRepository;

    private LiveData<List<Car>> carListLiveData;

    public RefuelListViewModel(@NonNull Application application) {
        super(application);
        db = FuelDatabase.getDatabase(application);

        carRepository = new CarRepository(application);
        fuelRepository = new FuelRepository(application);
        carListLiveData = carRepository.getAllCars();
    }

    public LiveData<List<Car>> getCars() {
        return carListLiveData;
    }

    public LiveData<List<Refuel>> getRefuels(int carId) {
        return fuelRepository.getRefuels(carId);
    }
}
