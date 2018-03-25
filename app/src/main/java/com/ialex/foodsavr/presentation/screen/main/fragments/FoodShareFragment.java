package com.ialex.foodsavr.presentation.screen.main.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ialex.foodsavr.R;
import com.ialex.foodsavr.component.FoodApplication;
import com.ialex.foodsavr.component.MiscUtils;
import com.ialex.foodsavr.component.RecyclerViewSpacingDecorator;
import com.ialex.foodsavr.data.DataRepository;
import com.ialex.foodsavr.data.remote.models.FridgeItem;
import com.ialex.foodsavr.data.remote.response.AddFridgeItemResponse;
import com.ialex.foodsavr.presentation.screen.main.DonationsAdapter;
import com.ialex.foodsavr.presentation.screen.main.FridgeAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alex on 24/03/2018.
 */

public class FoodShareFragment extends Fragment implements ProductListener {

    @BindView(R.id.donations_recycler)
    RecyclerView donationsRecyclerView;

    @BindView(R.id.charity_image)
    ImageView charityImage;

    @BindView(R.id.no_items_donated)
    TextView noItemsDonated;

    @Inject
    DataRepository dataRepository;

    private DonationsAdapter mAdapter;

    private List<FridgeItem> donatedItems = new ArrayList<>();

    public static FoodShareFragment newInstance() {
        FoodShareFragment fragment = new FoodShareFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share_food, container, false);
        ButterKnife.bind(this, view);

        FoodApplication.component().inject(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();
        dataRepository.getDonatedItems(this);
    }

    @Override
    public void onProductShared() {

    }

    @Override
    public void onProductAdded(AddFridgeItemResponse response) {

    }

    @Override
    public void onReceiveFridgeItems(List<FridgeItem> items) {

    }

    @Override
    public void onReceiveDonatedItems(List<FridgeItem> items) {
        mAdapter.setNewDataset(items);

        if (items.size() > 0) {
            hideInfo();
        } else {
            showInfo();
        }
    }

    @Override
    public void onError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    private void setupRecyclerView() {
        int padding = MiscUtils.dpToPx(8);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        donationsRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        donationsRecyclerView.setLayoutManager(mLayoutManager);

        donationsRecyclerView.addItemDecoration(new RecyclerViewSpacingDecorator(padding, padding));

        // specify an adapter (see also next example)
        mAdapter = new DonationsAdapter(donationsRecyclerView, getContext(), donatedItems);
        mAdapter.setHasStableIds(true);
        donationsRecyclerView.setAdapter(mAdapter);
    }

    private void hideInfo() {
        charityImage.setVisibility(View.GONE);
        noItemsDonated.setVisibility(View.GONE);
    }

    private void showInfo() {
        charityImage.setVisibility(View.VISIBLE);
        noItemsDonated.setVisibility(View.VISIBLE);
    }
}
