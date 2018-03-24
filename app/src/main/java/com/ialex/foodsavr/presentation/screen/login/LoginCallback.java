package com.ialex.foodsavr.presentation.screen.login;

/**
 * Created by alex on 24/03/2018.
 */

public interface LoginCallback {

    void onLoginSuccess();

    void onLoginError(String message);
}
