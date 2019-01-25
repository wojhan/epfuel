package com.github.wojhan.epfuel;

import java.util.Date;

class Refuel {
    private int id;
    private String type;
    private float amount;
    private float priceForLiter;
    private Date date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getPriceForLiter() {
        return priceForLiter;
    }

    public void setPriceForLiter(float priceForLiter) {
        this.priceForLiter = priceForLiter;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
