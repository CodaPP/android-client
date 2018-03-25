package com.ialex.foodsavr.presentation.screen.main.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ialex.foodsavr.R;
import com.ialex.foodsavr.component.FoodApplication;
import com.ialex.foodsavr.data.DataRepository;
import com.ialex.foodsavr.data.remote.models.RecipeItem;
import com.ialex.foodsavr.presentation.screen.main.RecipesCardAdapter;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.SwipeDirection;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by alex on 24/03/2018.
 */

public class RecipesFragment extends Fragment implements RecipesListener {

    @BindView(R.id.recipes_card_stack_view)
    CardStackView cardStackView;

    @Inject
    DataRepository dataRepository;

    private RecipesCardAdapter adapter;

    private List<RecipeItem> oldItems;

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

        dataRepository.getRecipes(this);
    }

    private RecipesCardAdapter createTouristSpotCardAdapter() {
        final RecipesCardAdapter adapter = new RecipesCardAdapter(getContext());
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
                    paginate();
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

    @Override
    public void onNewRecipes(List<RecipeItem> recipes) {
        adapter.addAll(recipes);
        adapter.notifyDataSetChanged();
        oldItems = recipes;
    }

    @Override
    public void onError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void paginate() {
        cardStackView.setPaginationReserved();
        adapter.addAll(oldItems);
        adapter.notifyDataSetChanged();
    }
}
