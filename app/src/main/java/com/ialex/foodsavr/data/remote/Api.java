package com.ialex.foodsavr.data.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ialex on 15.02.2017.
 */

public interface Api {
    String URL_PROD_BASE = "http://defcon33.ddns.net/";
    String URL_PROD_TEST = "http://yumpi.ddns.net/";

    /* @POST("/user/register")
    @Headers({"Cache-Control: no-store, no-cache", "User-Agent: android"})
    Observable<RegisterResponse> register(@Body RegisterBody body);

    @POST("/user/login")
    @Headers({"Cache-Control: no-store, no-cache", "User-Agent: android"})
    Observable<LoginResponse> login(@Body LoginBody body);

    @POST("/user/logout")
    @Headers({"Cache-Control: no-store, no-cache", "User-Agent: android"})
    Observable<ApiResponse> logout();

    @GET("/get/cardslist")
    @Headers({"Cache-Control: no-store, no-cache", "User-Agent: android"})
    Observable<GenericResponse> getCardsList();*/
}
