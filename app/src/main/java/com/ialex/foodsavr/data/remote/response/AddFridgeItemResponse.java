package com.ialex.foodsavr.data.remote.response;

import com.google.gson.annotations.SerializedName;
import com.ialex.foodsavr.data.remote.models.RequiredInfo;

/**
 * Created by alex on 24/03/2018.
 */

public class AddFridgeItemResponse extends BaseResponse {

    @SerializedName("requiredInfo")
    public RequiredInfo requiredInfo;
}
