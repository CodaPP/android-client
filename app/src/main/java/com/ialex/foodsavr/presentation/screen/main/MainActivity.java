package com.ialex.foodsavr.presentation.screen.main;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.ialex.foodsavr.R;
import com.ialex.foodsavr.component.FoodApplication;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.navigation)
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        MainComponent component = DaggerMainComponent.builder()
                .appComponent(FoodApplication.component())
                .build();
        component.inject(this);

        setupBottomNavigationView();
    }

    private void setupBottomNavigationView() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_share_food:
                        //switchToNearbyStations();
                        return true;
                    case R.id.item_fridge:
                        //switchToEvents();
                        return true;
                    case R.id.item_recipes:
                        //switchToMap();
                        return true;
                }

                return false;
            }
        });
    }
}
