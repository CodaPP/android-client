package com.ialex.foodsavr.data.remote.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alex on 24/03/2018.
 */

public class FridgeItem {

    @SerializedName("ID")
    public Integer itemId;

    @SerializedName("Quantity")
    public Integer quantity;

    @SerializedName("Barcode")
    public String barcode;

    @SerializedName("Manufacturer")
    public String manufacturer;

    @SerializedName("Name")
    public String name;

    @SerializedName("Photo")
    public String photo;

    @SerializedName("UseBy")
    public String useBy;

    @SerializedName("BestBefore")
    public String bestBefore;
}
