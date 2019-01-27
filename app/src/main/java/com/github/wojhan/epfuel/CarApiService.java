package com.github.wojhan.epfuel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CarApiService {
    String BASE_URL = "https://www.carqueryapi.com/api/0.3/";

    @GET("?cmd=getMakes")
    Call<MakeList> getMakes();

    @GET("?cmd=getModels")
    Call<ModelList> getModels(@Query("make") String make);
}
