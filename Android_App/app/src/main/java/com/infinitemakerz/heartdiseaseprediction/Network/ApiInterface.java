package com.infinitemakerz.heartdiseaseprediction.Network;

import com.infinitemakerz.heartdiseaseprediction.Model.Pojo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface
{
    @GET("heartpredict/viz")
    Call<Pojo> getAPIResponse(@Query("vizdata[]") List<String> vizdata);
}
