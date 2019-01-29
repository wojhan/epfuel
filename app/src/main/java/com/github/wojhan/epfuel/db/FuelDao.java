package com.github.wojhan.epfuel.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface FuelDao {
    @Insert
    void insert(Refuel refuel);

    @Update
    void update(Refuel refuel);

    @Delete
    void delete(Refuel refuel);

    @Query("SELECT * FROM refuel WHERE car_id=:carId ORDER BY date DESC")
    LiveData<List<Refuel>> getRefuels(int carId);

    @Query("SELECT * FROM refuel WHERE car_id=:carId AND fuel_type LIKE :fuelType ORDER BY date DESC")
    LiveData<List<Refuel>> getRefuelsByType(int carId, String fuelType);

    @Query("SELECT * FROM refuel WHERE car_id=:carId AND date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    LiveData<List<Refuel>> getRefuelsByMonth(int carId, Date startDate, Date endDate);
}
