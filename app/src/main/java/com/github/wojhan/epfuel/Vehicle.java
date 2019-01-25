package com.github.wojhan.epfuel;

public class Vehicle {

    private String name;
    private int image;

    public Vehicle () {

    }

    public Vehicle (String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
