package com.ialex.foodsavr.data;

import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ialex.foodsavr.data.local.prefs.PrefsRepository;
import com.ialex.foodsavr.data.remote.Api;
import com.ialex.foodsavr.data.util.UriSerialization;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ialex on 15.02.2017.
 */

@Module
public class DataModule {

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Uri.class, new UriSerialization());
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    DataRepository provideDataManager(Api rxApi, PrefsRepository prefsRepository) {
        return new DataRepository(rxApi, prefsRepository);
    }
}
