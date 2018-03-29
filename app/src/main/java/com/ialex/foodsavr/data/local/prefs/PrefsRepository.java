package com.ialex.foodsavr.data.local.prefs;

import com.google.gson.Gson;
import com.ialex.foodsavr.component.FoodApplication;
import com.ialex.foodsavr.data.local.prefs.util.StringPreference;
import com.ialex.foodsavr.data.local.prefs.util.StringSetPreference;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by ialex on 15.02.2017.
 */

public class PrefsRepository {

    @Inject
    @Named("username")
    StringPreference usernamePreference;

    @Inject
    @Named("password")
    StringPreference passwordPreference;

    @Inject
    @Named("name")
    StringPreference namePreference;

    @Inject
    @Named("laravel_session")
    StringPreference laravelSessionPreference;

    @Inject
    @Named("cookies")
    StringSetPreference cookiesPreference;

    @Inject
    Gson gson;

    public PrefsRepository() {
        FoodApplication.component().inject(this);
    }

    /**
     *
     */

    public String getUsername() {
        return usernamePreference.get();
    }

    public String getPassword() {
        return passwordPreference.get();
    }

    public String getName() {
        return namePreference.get();
    }

    public void setUsername(String username) {
        usernamePreference.set(username);
    }

    public void setPassword(String password) {
        passwordPreference.set(password);
    }

    public void setName(String name) {
        namePreference.set(name);
    }

    //

    public Set<String> getCookies() {
        return cookiesPreference.get();
    }

    public void saveCookies(Set<String> value) {
        cookiesPreference.set(value);
    }

    public void setLaravelToken(String token) {
        laravelSessionPreference.set(token);
    }

    public String getLaravelToken() {
        return laravelSessionPreference.get();
    }
}
