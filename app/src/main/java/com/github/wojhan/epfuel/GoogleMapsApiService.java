package com.github.wojhan.epfuel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleMapsApiService {
    String BASE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/";

    @GET("json?key=AIzaSyCcjMR1eX5eJys9letfsze3Z_9QBCtHxd0&radius=10000&type=gas_station")
    Call<GasStationList> getStations(@Query("location") String location);
}
