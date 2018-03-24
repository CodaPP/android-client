package com.ialex.foodsavr.data.remote.response;

import com.google.gson.annotations.SerializedName;
import com.ialex.foodsavr.data.remote.models.FridgeItem;

import java.util.List;

/**
 * Created by alex on 24/03/2018.
 */

public class ProductsResponse extends BaseResponse {

    @SerializedName("products")
    public List<FridgeItem> items;
}
