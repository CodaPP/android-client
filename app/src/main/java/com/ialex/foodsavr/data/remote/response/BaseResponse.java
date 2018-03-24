package com.ialex.foodsavr.data.remote.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alex on 24/03/2018.
 */

public class BaseResponse {

    @SerializedName("STATUS")
    public Boolean status;

    @SerializedName("ERROR")
    public String error;
}
