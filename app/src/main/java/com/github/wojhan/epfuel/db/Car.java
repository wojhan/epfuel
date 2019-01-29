package com.github.wojhan.epfuel.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Car {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "make")
    private String make;

    @ColumnInfo(name = "model")
    private String model;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "first_tank")
    private String firstTank;

    @ColumnInfo(name = "second_tank")
    private String secondTank;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFirstTank() {
        return firstTank;
    }

    public void setFirstTank(String firstTank) {
        this.firstTank = firstTank;
    }

    public String getSecondTank() {
        return secondTank;
    }

    public void setSecondTank(String secondTank) {
        this.secondTank = secondTank;
    }
}
