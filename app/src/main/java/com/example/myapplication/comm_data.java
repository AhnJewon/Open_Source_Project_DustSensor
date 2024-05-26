package com.example.myapplication;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface comm_data {

    @FormUrlEncoded
    @POST("dustsensor/sensing/")
    Call<String> post(
            @Field("sensor") String sensor,
            @Field("mac") String mac,
            @Field("receiver") String receiver,
            @Field("time") String time,
            @Field("otp") String otp,
            @Field("data") String data
    );

    @POST("dustsensor/sensing/")
    Call<postdata> post_json(
            @Body postdata pd
    );

    @GET("dustsensor/commtest_get/")
    Call<String> get(
            @Query("sensor") String sensor,
            @Query("mac") String mac,
            @Query("receiver") String receiver,
            @Query("time") int time,
            @Query("otp") int otp,
            @Query("data") String data
    );

    @FormUrlEncoded
    @POST("localization/locationcheck/")
    Call<String> location(
            @Field("wifidata") String data
    );

}



