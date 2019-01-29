package com.github.wojhan.epfuel.db;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import java.sql.Ref;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FuelRepository {

    private FuelDatabase fuelDatabase;
    private FuelDao fuelDao;

    public FuelRepository(Application application) {

        fuelDatabase = FuelDatabase.getDatabase(application);
        fuelDao = fuelDatabase.fuelDao();
    }

    public LiveData<List<Refuel>> getRefuels(int carId) {
        return fuelDao.getRefuels(carId);
    }

    public LiveData<List<Refuel>> getRefuelsByFuelType(int carId, String fuelType) {
        return fuelDao.getRefuelsByType(carId, fuelType);
    }

    public LiveData<List<Refuel>> getRefuelsByMonth(int carId, int month, int year) {
        Date startDate = new Date();
        Date endDate = new Date();
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        startDate.setTime(calendar.getTimeInMillis());

        calendar.set(Calendar.YEAR, month == 11 ? year + 1 : year);
        calendar.set(Calendar.MONTH, month == 11 ? 0 : month + 1);

        endDate.setTime(calendar.getTimeInMillis());

        return fuelDao.getRefuelsByMonth(carId, startDate, endDate);
    }

    public void insert(Refuel refuel) {
        InsertAsyncTask asyncTask = new InsertAsyncTask(fuelDao);
        asyncTask.execute(refuel);
    }

    private static class InsertAsyncTask extends AsyncTask<Refuel, Void, Void> {

        private FuelDao mAsyncTaskDao;

        private InsertAsyncTask(FuelDao asyncTaskDao) {
            mAsyncTaskDao = asyncTaskDao;
        }

        @Override
        protected Void doInBackground(Refuel... refuels) {
            mAsyncTaskDao.insert(refuels[0]);
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
