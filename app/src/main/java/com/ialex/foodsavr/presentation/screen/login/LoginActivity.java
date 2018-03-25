package com.ialex.foodsavr.presentation.screen.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.ialex.foodsavr.R;
import com.ialex.foodsavr.component.FoodApplication;
import com.ialex.foodsavr.component.MiscUtils;
import com.ialex.foodsavr.data.DataRepository;
import com.ialex.foodsavr.data.local.prefs.PrefsRepository;
import com.ialex.foodsavr.presentation.screen.main.MainActivity;
import com.sirvar.robin.RobinActivity;
import com.sirvar.robin.Theme;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by alex on 24/03/2018.
 */

public class LoginActivity extends RobinActivity implements LoginCallback {

    @Inject
    DataRepository dataRepository;

    @Inject
    PrefsRepository prefsRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // DO NOT call setContentView();
        FoodApplication.component().inject(this);

        // Set title for each screen
        setLoginTitle("Sign in to FoodSavr");
        setSignupTitle("Welcome to FoodSavr");
        setForgotPasswordTitle("Forgot Password");

        // Set logo for screens
        setImage(getResources().getDrawable(R.mipmap.ic_launcher));

        // Use custom font across all views
        //setFont(Typeface.createFromAsset(getAssets(), "Montserrat-Medium.ttf"));

        // Choose theme (default is LIGHT)
        setTheme(Theme.LIGHT);

        disableSocialLogin();
    }

    @Override
    protected void onLogin(String email, String password) {
        if (!MiscUtils.isConnectedToInternet(this)) {
            Toast.makeText(this, "You are not connected to the internet", Toast.LENGTH_SHORT).show();
            return;
        }

        Timber.d( "token %s", FirebaseInstanceId.getInstance().getToken());

        prefsRepository.setUsername(email);
        prefsRepository.setPassword(password);

        dataRepository.login(email, password, this);
    }

    @Override
    protected void onSignup(String name, String email, String password) {
        if (!MiscUtils.isConnectedToInternet(this)) {
            Toast.makeText(this, "You are not connected to the internet", Toast.LENGTH_SHORT).show();
            return;
        }

        prefsRepository.setName(name);
        prefsRepository.setUsername(email);
        prefsRepository.setPassword(password);

        dataRepository.register(name, email, password, this);
    }

    @Override
    protected void onForgotPassword(String email) {

    }

    @Override
    protected void onGoogleLogin() {

    }

    @Override
    protected void onFacebookLogin() {

    }

    @Override
    public void onLoginSuccess() {
        dataRepository.setFirebaseRefreshToken(FirebaseInstanceId.getInstance().getToken());

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }

    @Override
    public void onLoginError(String message) {
        prefsRepository.setName(null);
        prefsRepository.setUsername(null);
        prefsRepository.setPassword(null);

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
