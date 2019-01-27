package com.github.wojhan.epfuel;

import com.google.gson.annotations.SerializedName;

class Model {
    @SerializedName("model_name")
    private String name;

    public String getName() {
        return name;
    }
}
