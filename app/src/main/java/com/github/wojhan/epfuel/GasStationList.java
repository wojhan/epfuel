package com.github.wojhan.epfuel;

import com.google.gson.annotations.SerializedName;

class GasStationList {
    @SerializedName("results")
    GasStation[] gasStations;

    public GasStation[] getGasStations() {
        return gasStations;
    }
}
