package com.github.wojhan.epfuel;

import com.google.gson.annotations.SerializedName;

class Make {
    @SerializedName("make_id")
    private String id;

    @SerializedName("make_display")
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}