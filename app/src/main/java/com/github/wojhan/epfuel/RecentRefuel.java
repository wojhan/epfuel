package com.github.wojhan.epfuel;

import java.util.Date;

public class RecentRefuel {
    private String type;
    private float AvgConsumption;
    private float LastConsumption;
    private float LastPrice;
    private Date date;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getAvgConsumption() {
        return AvgConsumption;
    }

    public void setAvgConsumption(float avgConsumption) {
        AvgConsumption = avgConsumption;
    }

    public float getLastConsumption() {
        return LastConsumption;
    }

    public void setLastConsumption(float lastConsumption) {
        LastConsumption = lastConsumption;
    }

    public float getLastPrice() {
        return LastPrice;
    }

    public void setLastPrice(float lastPrice) {
        LastPrice = lastPrice;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
