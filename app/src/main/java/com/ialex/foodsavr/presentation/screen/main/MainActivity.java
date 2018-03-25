package com.ialex.foodsavr.presentation.screen.main;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ialex.foodsavr.R;
import com.ialex.foodsavr.component.FoodApplication;
import com.ialex.foodsavr.data.DataRepository;
import com.ialex.foodsavr.data.local.prefs.PrefsRepository;
import com.ialex.foodsavr.presentation.screen.barcode.BarcodeActivity;
import com.ialex.foodsavr.presentation.screen.login.LoginActivity;
import com.ialex.foodsavr.presentation.screen.main.fragments.FoodShareFragment;
import com.ialex.foodsavr.presentation.screen.main.fragments.FridgeFragment;
import com.ialex.foodsavr.presentation.screen.main.fragments.RecipesFragment;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import timber.log.Timber;

@RuntimePermissions
public class MainActivity extends AppCompatActivity implements SignOutListener {

    private final int RC_SCAN_BARCODE = 10;

    @BindView(R.id.navigation)
    BottomNavigationView bottomNavigationView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

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
            onSignOut();
            return;
        }

        setSupportActionBar(toolbar);

        setupBottomNavigationView();
        setupNavigationDrawer();

        bottomNavigationView.setSelectedItemId(R.id.item_fridge);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SCAN_BARCODE) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra(BarcodeActivity.EXTRA_BARCODE);

                if (result != null) {
                    broadcastScannedBarcode(result);
                }
            }
        }
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    void showScannerActivity() {
        Intent intent = new Intent(this, BarcodeActivity.class);
        startActivityForResult(intent, RC_SCAN_BARCODE);
    }

    public void showScannerWrapper() {
        MainActivityPermissionsDispatcher.showScannerActivityWithPermissionCheck(this);
    }

    public void broadcastScannedBarcode(String barcode) {
        if (fridgeFragment != null) {
            fridgeFragment.onBarcodeScanned(barcode);
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
                        switchToRecipes();
                        return true;
                }

                return false;
            }
        });
    }

    private void setupNavigationDrawer() {
        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header2)
                .addProfiles(
                        new ProfileDrawerItem().withName(prefsRepository.getName()).withEmail(prefsRepository.getUsername()).withIcon(getResources().getDrawable(R.drawable.profile))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        final PrimaryDrawerItem logout = new PrimaryDrawerItem().withIdentifier(1).withName("Logout");


        //create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .withToolbar(toolbar)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        if (drawerItem.getIdentifier() == 1) {
                            logout();
                        }

                        return true;
                    }
                })
                .build();

        result.addStickyFooterItem(logout);
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

    private void switchToRecipes() {
        if (recipesFragment == null) {
            recipesFragment = RecipesFragment.newInstance();
        }

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragment_container, recipesFragment).commit();
    }

    private void logout() {
        dataRepository.logout(this);
    }

    @Override
    public void onSignOut() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

        finish();
    }

    public FridgeFragment getFridgeFragment() {
        return fridgeFragment;
    }
}
