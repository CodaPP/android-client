package com.ialex.foodsavr.data.local.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ialex.foodsavr.data.local.prefs.util.LongPreference;
import com.ialex.foodsavr.data.local.prefs.util.StringPreference;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ialex on 15.02.2017.
 */

@Module
public class PrefsModule {

    @Provides
    @Singleton
    PrefsRepository providePrefsRepository() {
        return new PrefsRepository();
    }

    @Provides
    @Singleton
    SharedPreferences provideAppSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @Named("username")
    @Singleton
    StringPreference provideUsernamePreference(SharedPreferences preferences) {
        return new StringPreference(preferences, "username", null);
    }

    @Provides
    @Named("password")
    @Singleton
    StringPreference providePasswordData(SharedPreferences preferences) {
        return new StringPreference(preferences, "password", null);
    }
}
