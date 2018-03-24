package com.ialex.foodsavr.data.remote;

import com.ialex.foodsavr.data.remote.response.AddFridgeItemResponse;
import com.ialex.foodsavr.data.remote.response.BaseResponse;
import com.ialex.foodsavr.data.remote.response.ProductsResponse;
import com.ialex.foodsavr.data.remote.response.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by ialex on 15.02.2017.
 */

public interface Api {
    String URL_PROD_BASE = "http://defcon33.ddns.net/";
    String URL_PROD_TEST = "http://192.168.43.46/";

    @FormUrlEncoded
    @POST("/FoodSavr/public/register")
    @Headers({"Cache-Control: no-store, no-cache", "User-Agent: android"})
    Call<RegisterResponse> register(@Field("name") String name,
                                    @Field("email") String email,
                                    @Field("password") String password);

    @FormUrlEncoded
    @POST("/FoodSavr/public/login")
    @Headers({"Cache-Control: no-store, no-cache", "User-Agent: android"})
    Call<RegisterResponse> login(@Field("email") String email,
                                 @Field("password") String password);

    @POST("/FoodSavr/public/logout")
    @Headers({"Cache-Control: no-store, no-cache", "User-Agent: android"})
    Call<AddFridgeItemResponse> logout();

    @FormUrlEncoded
    @POST("/FoodSavr/public/user/addFridgeItems")
    @Headers({"Cache-Control: no-store, no-cache", "User-Agent: android"})
    Call<AddFridgeItemResponse> addFridgeItem(@Field("barcode") String barcode,
                                              @Field("quantity") Integer quantity,
                                              @Field("useBy") Long useBy);

    @FormUrlEncoded
    @POST("/FoodSavr/public/products/updateInfo")
    @Headers({"Cache-Control: no-store, no-cache", "User-Agent: android"})
    Call<BaseResponse> updateItemInfo(@Field("barcode") String barcode,
                                        @Field("manufacturer") String manufacturer,
                                        @Field("name") String name);

    @GET("/FoodSavr/public/login")
    @Headers({"Cache-Control: no-store, no-cache", "User-Agent: android"})
    Call<RegisterResponse> testLogin();

    @GET("/FoodSavr/public/user/getFridgeItems")
    @Headers({"Cache-Control: no-store, no-cache", "User-Agent: android"})
    Call<ProductsResponse> getFridgeItems();

    /*@POST("/user/login")
    @Headers({"Cache-Control: no-store, no-cache", "User-Agent: android"})
    Observable<LoginResponse> login(@Body LoginBody body);

    @POST("/user/logout")
    @Headers({"Cache-Control: no-store, no-cache", "User-Agent: android"})
    Observable<ApiResponse> logout();

    @GET("/get/cardslist")
    @Headers({"Cache-Control: no-store, no-cache", "User-Agent: android"})
    Observable<GenericResponse> getCardsList();*/
}
