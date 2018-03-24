package com.ialex.foodsavr.presentation.screen;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Toast;

import com.ialex.foodsavr.R;
import com.ialex.foodsavr.component.FoodApplication;
import com.ialex.foodsavr.component.MiscUtils;
import com.ialex.foodsavr.data.DataRepository;
import com.sirvar.robin.RobinActivity;
import com.sirvar.robin.Theme;

import javax.inject.Inject;

/**
 * Created by alex on 24/03/2018.
 */

public class LoginActivity extends RobinActivity {

    @Inject
    DataRepository dataRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // DO NOT call setContentView();
        FoodApplication.component().inject(this);

        // Set title for each screen
        setLoginTitle("Sign in to Robin");
        setSignupTitle("Welcome to Robin");
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

        dataRepository.login(email, password);
    }

    @Override
    protected void onSignup(String name, String email, String password) {
        if (!MiscUtils.isConnectedToInternet(this)) {
            Toast.makeText(this, "You are not connected to the internet", Toast.LENGTH_SHORT).show();
            return;
        }

        dataRepository.register(name, email, password);
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
}
