package com.ialex.foodsavr.presentation.screen.details;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ialex.foodsavr.R;
import com.ialex.foodsavr.component.MiscUtils;
import com.ialex.foodsavr.component.RecyclerViewSpacingDecorator;
import com.ialex.foodsavr.data.remote.models.RecipeItem;
import com.ialex.foodsavr.presentation.screen.main.FridgeAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailsActivity extends AppCompatActivity {

    public static String EXTRA_RECIPE_INFO = "recipe_info";

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.ingredientRecycler)
    RecyclerView ingredientsRecycer;

    @BindView(R.id.header_image)
    ImageView headerImage;

    private RecipeItem recipeItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadData();
        setContentView(R.layout.activity_recipe_details);

        ButterKnife.bind(this);

        RequestOptions options = new RequestOptions()
                .centerCrop();

        setSupportActionBar(toolbar);

        Glide.with(this).load(recipeItem.photo).apply(options).into(headerImage);
        collapsingToolbar.setTitle(recipeItem.name);
        collapsingToolbar.setStatusBarScrimResource(R.color.primary);

        setupRecycler();
    }

    private void loadData() {
        Bundle extras = getIntent().getExtras();

        if (extras == null) {
            finish();
            return;
        }

        recipeItem = extras.getParcelable(EXTRA_RECIPE_INFO);
    }

    private void setupRecycler() {
        int padding = MiscUtils.dpToPx(8);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        ingredientsRecycer.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        ingredientsRecycer.setLayoutManager(mLayoutManager);

        ingredientsRecycer.addItemDecoration(new RecyclerViewSpacingDecorator(padding, padding));

        // specify an adapter (see also next example)
        IngredientsAdapter adapter = new IngredientsAdapter(this, recipeItem.ingredients);
        adapter.setHasStableIds(true);
        ingredientsRecycer.setAdapter(adapter);
    }
}
