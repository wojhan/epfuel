package com.github.wojhan.epfuel;

import com.google.gson.annotations.SerializedName;

class MakeList {
    @SerializedName("Makes")
    private Make[] makes;

    public Make[] getMakes() {
        return makes;
    }
}