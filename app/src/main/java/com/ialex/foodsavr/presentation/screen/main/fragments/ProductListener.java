package com.ialex.foodsavr.presentation.screen.main.fragments;

import com.ialex.foodsavr.data.remote.models.FridgeItem;
import com.ialex.foodsavr.data.remote.response.AddFridgeItemResponse;

import java.util.List;

/**
 * Created by alex on 24/03/2018.
 */

public interface ProductListener {

    void onProductAdded(AddFridgeItemResponse response);

    void onReceiveFridgeItems(List<FridgeItem> items);

    void onError(String error);
}
