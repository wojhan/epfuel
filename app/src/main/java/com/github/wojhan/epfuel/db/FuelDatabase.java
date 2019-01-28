package com.github.wojhan.epfuel.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Car.class, Refuel.class}, version = 1)
public abstract class FuelDatabase extends RoomDatabase {
    public abstract CarDao carDao();

    public abstract FuelDao fuelDao();
}
