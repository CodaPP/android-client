package com.ialex.foodsavr.presentation.screen.main.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.zxing.integration.android.IntentResult;
import com.ialex.foodsavr.R;
import com.ialex.foodsavr.component.FoodApplication;
import com.ialex.foodsavr.component.MiscUtils;
import com.ialex.foodsavr.component.RecyclerViewSpacingDecorator;
import com.ialex.foodsavr.data.DataRepository;
import com.ialex.foodsavr.data.remote.models.FridgeItem;
import com.ialex.foodsavr.data.remote.models.TempItemInfo;
import com.ialex.foodsavr.data.remote.response.AddFridgeItemResponse;
import com.ialex.foodsavr.presentation.screen.main.FridgeAdapter;
import com.ialex.foodsavr.presentation.screen.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by alex on 24/03/2018.
 */

public class FridgeFragment extends Fragment implements ProductListener {

    @BindView(R.id.fridge_recycler)
    RecyclerView fridgeRecyclerView;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    @Inject
    DataRepository dataRepository;
    
    private FridgeAdapter mAdapter;

    private List<FridgeItem> fridgeItems = new ArrayList<>();

    public static FridgeFragment newInstance() {
        FridgeFragment fragment = new FridgeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fridge, container, false);
        ButterKnife.bind(this, view);

        FoodApplication.component().inject(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();
        setupRefresh();
        dataRepository.getFridgeItems(this);
    }

    @OnClick(R.id.fab_add_item)
    void addFridgeItem() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showScannerWrapper();
        }
    }

    public void onBarcodeScanned(IntentResult result) {
        Toast.makeText(getContext(), "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

        dataRepository.addFridgeItem(result.getContents(), this);
    }

    private void setupRecyclerView() {
        int padding = MiscUtils.dpToPx(8);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        fridgeRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        fridgeRecyclerView.setLayoutManager(mLayoutManager);

        fridgeRecyclerView.addItemDecoration(new RecyclerViewSpacingDecorator(padding, padding));

        // specify an adapter (see also next example)
        mAdapter = new FridgeAdapter(fridgeRecyclerView, getContext(), fridgeItems);
        mAdapter.setHasStableIds(true);
        fridgeRecyclerView.setAdapter(mAdapter);
    }

    private void setupRefresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataRepository.getFridgeItems(FridgeFragment.this);
            }
        });
    }

    @Override
    public void onProductAdded(AddFridgeItemResponse response) {
        askNextRequiredInfo(response, new TempItemInfo());
    }

    @Override
    public void onReceiveFridgeItems(List<FridgeItem> items) {
        swipeRefreshLayout.setRefreshing(false);
        mAdapter.setNewDataset(items);
    }

    @Override
    public void onError(String error) {
        swipeRefreshLayout.setRefreshing(false);
    }

    private void askNextRequiredInfo(final AddFridgeItemResponse response, final TempItemInfo tempItemInfo) {
        if (response.requiredInfo.manufacturer) {
            new MaterialDialog.Builder(getContext())
                    .title(R.string.dialog_title_manufacturer)
                    .content(R.string.dialog_content_manufacturer)
                    .inputType(InputType.TYPE_CLASS_TEXT)
                    .input(R.string.dialog_hint_manufacturer, R.string.dialog_prefill_manufacturer, new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                            // Do something
                            response.requiredInfo.manufacturer = false;
                            tempItemInfo.manufacturer = input.toString();

                            askNextRequiredInfo(response, tempItemInfo);
                        }
                    })
                    .show();

            return;
        }

        if (response.requiredInfo.name) {
            new MaterialDialog.Builder(getContext())
                    .title(R.string.dialog_title_name)
                    .content(R.string.dialog_content_name)
                    .inputType(InputType.TYPE_CLASS_TEXT)
                    .input(R.string.dialog_hint_name, R.string.dialog_prefill_name, new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                            // Do something
                            response.requiredInfo.name = false;
                            tempItemInfo.name = input.toString();

                            askNextRequiredInfo(response, tempItemInfo);
                        }
                    })
                    .show();

            return;
        }

        if (tempItemInfo.manufacturer == null && tempItemInfo.name == null) {
            return;
        }

        dataRepository.updateItemInfo(response.requiredInfo.barcode, tempItemInfo.manufacturer, tempItemInfo.name);
    }
}
