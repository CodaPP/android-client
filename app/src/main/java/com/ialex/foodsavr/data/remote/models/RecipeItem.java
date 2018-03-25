package com.ialex.foodsavr.data.remote.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alex on 25/03/2018.
 */

public class RecipeItem {

    @SerializedName("ID")
    public Integer id;

    @SerializedName("Name")
    public String name;

    @SerializedName("Photo")
    public String photo;

    @SerializedName("Ingredients")
    public List<String> ingredients;
}
