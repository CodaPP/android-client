package com.ialex.foodsavr.data.remote.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alex on 25/03/2018.
 */

public class RequiredInfo {

    @SerializedName("barcode")
    public String barcode;

    @SerializedName("manufacturer")
    public Boolean manufacturer;

    @SerializedName("name")
    public Boolean name;
}
