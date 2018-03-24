package com.ialex.foodsavr.data.remote.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alex on 24/03/2018.
 */

public class RegisterResponse extends BaseResponse {

    @SerializedName("USER_NAME")
    public String name;
}
