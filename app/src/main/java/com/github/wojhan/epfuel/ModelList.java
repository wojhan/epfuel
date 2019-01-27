package com.github.wojhan.epfuel;

import com.google.gson.annotations.SerializedName;

class ModelList {
    @SerializedName("Models")
    private Model[] models;

    public Model[] getModels() {
        return models;
    }
}
