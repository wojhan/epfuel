package com.github.wojhan.epfuel.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface FuelDao {
    @Insert
    void insert(Refuel refuel);

    @Update
    void update(Refuel refuel);

    @Delete
    void delete(Refuel refuel);

    @Query("SELECT * FROM refuel WHERE car_id=:carId ORDER BY id DESC")
    LiveData<List<Refuel>> getRefuels(int carId);
}
