package com.ialex.foodsavr.presentation.screen.main.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ialex.foodsavr.R;
import com.ialex.foodsavr.component.FoodApplication;
import com.ialex.foodsavr.data.remote.models.RecipeItem;
import com.ialex.foodsavr.presentation.screen.main.RecipesCardAdapter;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.SwipeDirection;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by alex on 24/03/2018.
 */

public class RecipesFragment extends Fragment {

    @BindView(R.id.recipes_card_stack_view)
    CardStackView cardStackView;

    private RecipesCardAdapter adapter;

    public static RecipesFragment newInstance() {
        RecipesFragment fragment = new RecipesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipes, container, false);
        ButterKnife.bind(this, view);

        FoodApplication.component().inject(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupCardStack();
    }

    private List<RecipeItem> createTouristSpots() {
        List<RecipeItem> spots = new ArrayList<>();
        spots.add(new RecipeItem("Yasaka Shrine", "Kyoto", "https://source.unsplash.com/Xq1ntWruZQI/600x800"));
        spots.add(new RecipeItem("Fushimi Inari Shrine", "Kyoto", "https://source.unsplash.com/NYyCqdBOKwc/600x800"));
        spots.add(new RecipeItem("Bamboo Forest", "Kyoto", "https://source.unsplash.com/buF62ewDLcQ/600x800"));
        spots.add(new RecipeItem("Brooklyn Bridge", "New York", "https://source.unsplash.com/THozNzxEP3g/600x800"));
        spots.add(new RecipeItem("Empire State Building", "New York", "https://source.unsplash.com/USrZRcRS2Lw/600x800"));
        spots.add(new RecipeItem("The statue of Liberty", "New York", "https://source.unsplash.com/PeFk7fzxTdk/600x800"));
        spots.add(new RecipeItem("Louvre Museum", "Paris", "https://source.unsplash.com/LrMWHKqilUw/600x800"));
        spots.add(new RecipeItem("Eiffel Tower", "Paris", "https://source.unsplash.com/HN-5Z6AmxrM/600x800"));
        spots.add(new RecipeItem("Big Ben", "London", "https://source.unsplash.com/CdVAUADdqEc/600x800"));
        spots.add(new RecipeItem("Great Wall of China", "China", "https://source.unsplash.com/AWh9C-QjhE4/600x800"));
        return spots;
    }

    private RecipesCardAdapter createTouristSpotCardAdapter() {
        final RecipesCardAdapter adapter = new RecipesCardAdapter(getContext());
        adapter.addAll(createTouristSpots());
        return adapter;
    }

    private void setupCardStack() {
        cardStackView.setCardEventListener(new CardStackView.CardEventListener() {
            @Override
            public void onCardDragging(float percentX, float percentY) {
                Timber.d("onCardDragging");
            }

            @Override
            public void onCardSwiped(SwipeDirection direction) {
                Timber.d("onCardSwiped: " + direction.toString());
                Timber.d("topIndex: " + cardStackView.getTopIndex());
                if (cardStackView.getTopIndex() == adapter.getCount() - 5) {
                    Timber.d("Paginate: " + cardStackView.getTopIndex());
                    //paginate();
                }
            }

            @Override
            public void onCardReversed() {
                Timber.d("onCardReversed");
            }

            @Override
            public void onCardMovedToOrigin() {
                Timber.d("onCardMovedToOrigin");
            }

            @Override
            public void onCardClicked(int index) {
                Timber.d("onCardClicked: " + index);
            }
        });

        adapter = createTouristSpotCardAdapter();
        cardStackView.setAdapter(adapter);
        cardStackView.setVisibility(View.VISIBLE);
    }
}
