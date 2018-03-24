package com.ialex.foodsavr.presentation.screen.main.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ialex.foodsavr.R;
import com.ialex.foodsavr.component.FoodApplication;

import butterknife.ButterKnife;

/**
 * Created by alex on 24/03/2018.
 */

public class FridgeFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nearby_stations, container, false);
        ButterKnife.bind(this, view);

        FoodApplication.component().inject(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
