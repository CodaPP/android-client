package com.ialex.foodsavr.presentation.screen.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.ialex.foodsavr.R;
import com.ialex.foodsavr.component.FoodApplication;
import com.ialex.foodsavr.data.local.prefs.PrefsRepository;
import com.ialex.foodsavr.presentation.screen.login.LoginActivity;
import com.ialex.foodsavr.presentation.screen.main.fragments.FoodShareFragment;
import com.ialex.foodsavr.presentation.screen.main.fragments.FridgeFragment;
import com.ialex.foodsavr.presentation.screen.main.fragments.RecipesFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.navigation)
    BottomNavigationView bottomNavigationView;

    @Inject
    PrefsRepository prefsRepository;

    private FridgeFragment fridgeFragment;
    private FoodShareFragment foodShareFragment;
    private RecipesFragment recipesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        MainComponent component = DaggerMainComponent.builder()
                .appComponent(FoodApplication.component())
                .build();
        component.inject(this);

        if (prefsRepository.getPassword() == null || prefsRepository.getUsername() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

            finish();
            return;
        }

        setupBottomNavigationView();
    }

    private void setupBottomNavigationView() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_share_food:
                        swithToFoodShare();
                        return true;
                    case R.id.item_fridge:
                        switchToFridge();
                        return true;
                    case R.id.item_recipes:
                        swithToRecipes();
                        return true;
                }

                return false;
            }
        });
    }

    private void switchToFridge() {
        if (fridgeFragment == null) {
            fridgeFragment = FridgeFragment.newInstance();
        }

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragment_container, fridgeFragment).commit();
    }

    private void swithToFoodShare() {
        if (foodShareFragment == null) {
            foodShareFragment = FoodShareFragment.newInstance();
        }

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragment_container, foodShareFragment).commit();
    }

    private void swithToRecipes() {
        if (recipesFragment == null) {
            recipesFragment = RecipesFragment.newInstance();
        }

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragment_container, recipesFragment).commit();
    }
}
