package com.ialex.foodsavr.presentation.screen.main.fragments;

import com.ialex.foodsavr.data.remote.models.FridgeItem;

import java.util.List;

/**
 * Created by alex on 24/03/2018.
 */

public interface ProductListener {

    void onProductAdded();

    void onReceiveFridgeItems(List<FridgeItem> items);

    void onError(String error);
}
