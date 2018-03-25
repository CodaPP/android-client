package com.ialex.foodsavr.data.remote.models;

/**
 * Created by alex on 25/03/2018.
 */

public class RecipeItem {

    public String name;

    public String url;

    public String description;

    public RecipeItem(String name, String description, String url) {
        this.name = name;
        this.description = description;
        this.url = url;
    }
}
