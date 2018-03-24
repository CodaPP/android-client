package com.ialex.foodsavr.presentation.screen.main;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.ialex.foodsavr.R;
import com.ialex.foodsavr.component.FoodApplication;
import com.ialex.foodsavr.data.DataRepository;
import com.ialex.foodsavr.data.local.prefs.PrefsRepository;
import com.ialex.foodsavr.presentation.screen.login.LoginActivity;
import com.ialex.foodsavr.presentation.screen.main.fragments.FoodShareFragment;
import com.ialex.foodsavr.presentation.screen.main.fragments.FridgeFragment;
import com.ialex.foodsavr.presentation.screen.main.fragments.RecipesFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import timber.log.Timber;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.navigation)
    BottomNavigationView bottomNavigationView;

    @Inject
    PrefsRepository prefsRepository;

    @Inject
    DataRepository dataRepository;

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

        dataRepository.getFridgeItems();

        setupBottomNavigationView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Timber.d(result.getContents());

                broadcastScannedBarcode(result);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    void showScannerActivity() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Scan a barcode");
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    public void showScannerWrapper() {
        MainActivityPermissionsDispatcher.showScannerActivityWithPermissionCheck(this);
    }

    public void broadcastScannedBarcode(IntentResult result) {
        if (fridgeFragment != null) {
            fridgeFragment.onBarcodeScanned(result);
        }
    }

    private void setupBottomNavigationView() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_share_food:
                        switchToFoodShare();
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

    private void switchToFoodShare() {
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
