package com.github.wojhan.epfuel.db;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import java.util.List;

public class CarRepository {

    private FuelDatabase fuelDatabase;
    private CarDao carDao;

    private LiveData<List<Car>> carListLiveData;

    public CarRepository(Application application) {

        fuelDatabase = FuelDatabase.getDatabase(application);
        carDao = fuelDatabase.carDao();
        carListLiveData = carDao.getAll();
    }

    public LiveData<List<Car>> getAllCars() {
        return carListLiveData;
    }

    public void insert(Car car) {
        InsertAsyncTask asyncTask = new InsertAsyncTask(carDao);
        asyncTask.execute(car);
    }

    public void update(Car car) {
        UpdateAsyncTask asyncTask = new UpdateAsyncTask(carDao);
        asyncTask.execute(car);
    }

    public void delete(Car car) {
        DeleteAsyncTask asyncTask = new DeleteAsyncTask(carDao);
        asyncTask.execute(car);
    }

    private static class InsertAsyncTask extends AsyncTask<Car, Void, Void> {

        private CarDao mAsyncTaskDao;

        private InsertAsyncTask(CarDao asyncTaskDao) {
            mAsyncTaskDao = asyncTaskDao;
        }

        @Override
        protected Void doInBackground(Car... cars) {
            mAsyncTaskDao.insert(cars[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Car, Void, Void> {
        private CarDao mAsyncTaskDao;

        private UpdateAsyncTask(CarDao asyncTask) {
            mAsyncTaskDao = asyncTask;
        }

        @Override
        protected Void doInBackground(Car... cars) {
            mAsyncTaskDao.update(cars[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Car, Void, Void> {
        private CarDao mAsyncTaskDao;

        private DeleteAsyncTask(CarDao asyncTask) {
            mAsyncTaskDao = asyncTask;
        }

        @Override
        protected Void doInBackground(Car... cars) {
            mAsyncTaskDao.delete(cars[0]);
            return null;
        }
    }

//    private class insertAsyncTask internal constructor(private val mAsyncTaskDao: HabitDao) : AsyncTask<Habit, Void, Void>() {
//
//        override fun doInBackground(vararg params: Habit): Void? {
//                mAsyncTaskDao.insert(params[0])
//        return null
//        }
//    }
}
