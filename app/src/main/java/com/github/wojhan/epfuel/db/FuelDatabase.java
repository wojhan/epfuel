package com.github.wojhan.epfuel.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(entities = {Car.class, Refuel.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class FuelDatabase extends RoomDatabase {

    public abstract CarDao carDao();
    public abstract FuelDao fuelDao();

    private static FuelDatabase INSTANCE;

    public static FuelDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FuelDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FuelDatabase.class, "fuel")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
