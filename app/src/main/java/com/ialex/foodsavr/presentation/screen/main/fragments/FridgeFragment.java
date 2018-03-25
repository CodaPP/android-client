package com.ialex.foodsavr.presentation.screen.main.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
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
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

/**
 * Created by alex on 24/03/2018.
 */

public class FridgeFragment extends Fragment implements ProductListener {

    @BindView(R.id.fridge_recycler)
    RecyclerView fridgeRecyclerView;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.fab_add_item)
    FloatingActionButton fab;

    @BindView(R.id.konfetti)
    KonfettiView konfettiView;

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

    @Override
    public void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showFab();
            }
        }, 300);
    }

    public void onBarcodeScanned(String barcode) {
        Toast.makeText(getContext(), "Scanned: " + barcode, Toast.LENGTH_LONG).show();

        dataRepository.addFridgeItem(barcode, this);
    }

    private void showFab() {
        ScaleAnimation anim = new ScaleAnimation(0,1,0,1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1f);
        anim.setFillBefore(true);
        anim.setFillAfter(true);
        anim.setFillEnabled(true);
        anim.setDuration(300);
        anim.setInterpolator(new OvershootInterpolator());
        fab.startAnimation(anim);

        fab.setVisibility(View.VISIBLE);
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
    public void onProductShared() {
        konfettiBurst();
        dataRepository.getFridgeItems(this);
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

    public void donate(int itemId, int quantity) {
        dataRepository.donateItems(itemId, quantity, this);
    }

    private void askNextRequiredInfo(final AddFridgeItemResponse response, final TempItemInfo tempItemInfo) {
        if (getContext() == null) {
            dataRepository.getFridgeItems(this);
            return;
        }

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
            //refresh the items
            dataRepository.getFridgeItems(this);
            return;
        }

        Toast.makeText(getContext(), "Va multumim pentru feedback!", Toast.LENGTH_SHORT).show();

        dataRepository.updateItemInfo(response.requiredInfo.barcode, tempItemInfo.manufacturer, tempItemInfo.name);
        dataRepository.getFridgeItems(this);
    }

    public void konfettiBurst() {
        konfettiView.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                .stream(150, 2000L);
    }
}
