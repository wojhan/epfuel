package com.github.wojhan.epfuel.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(foreignKeys = {
        @ForeignKey(entity = Car.class,
                parentColumns = "id",
                childColumns = "car_id")
})
public class Refuel {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "car_id")
    private int carId;

    @ColumnInfo(name = "fuel_type")
    private String type;

    @ColumnInfo(name = "counter")
    private int counter;

    @ColumnInfo(name = "price_for_liter")
    private float priceForLiter;

    @ColumnInfo(name = "amount")
    private float amount;

    @ColumnInfo(name = "date")
    private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public float getPriceForLiter() {
        return priceForLiter;
    }

    public void setPriceForLiter(float priceForLiter) {
        this.priceForLiter = priceForLiter;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
