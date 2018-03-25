package com.ialex.foodsavr.presentation.screen.main.fragments;

import com.ialex.foodsavr.data.remote.models.RecipeItem;

import java.util.List;

/**
 * Created by alex on 25/03/2018.
 */

public interface RecipesListener {

    void onNewRecipes(List<RecipeItem> recipes);

    void onError(String message);
}
