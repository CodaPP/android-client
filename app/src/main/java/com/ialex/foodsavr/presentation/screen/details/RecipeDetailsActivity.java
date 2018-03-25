package com.ialex.foodsavr.presentation.screen.details;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ialex.foodsavr.R;
import com.ialex.foodsavr.data.remote.models.RecipeItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailsActivity extends AppCompatActivity {

    public static String EXTRA_RECIPE_INFO = "recipe_info";

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

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
    }

    private void loadData() {
        Bundle extras = getIntent().getExtras();

        if (extras == null) {
            finish();
            return;
        }

        recipeItem = extras.getParcelable(EXTRA_RECIPE_INFO);

    }
}
