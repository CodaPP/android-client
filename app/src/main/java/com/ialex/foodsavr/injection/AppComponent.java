package com.ialex.foodsavr.injection;

import android.content.Context;

import com.ialex.foodsavr.component.FoodApplication;
import com.ialex.foodsavr.component.MyFirebaseIdService;
import com.ialex.foodsavr.data.DataModule;
import com.ialex.foodsavr.data.DataRepository;
import com.ialex.foodsavr.data.local.prefs.PrefsModule;
import com.ialex.foodsavr.data.local.prefs.PrefsRepository;
import com.ialex.foodsavr.data.remote.RemoteModule;
import com.ialex.foodsavr.presentation.screen.login.LoginActivity;
import com.ialex.foodsavr.presentation.screen.main.FridgeAdapter;
import com.ialex.foodsavr.presentation.screen.main.fragments.FoodShareFragment;
import com.ialex.foodsavr.presentation.screen.main.fragments.FridgeFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, DataModule.class, RemoteModule.class, PrefsModule.class})
public interface AppComponent {

    void inject(FoodApplication application);

    void inject(PrefsRepository prefsRepository);

    void inject(FridgeFragment fragment);

    void inject(LoginActivity activity);

    void inject(MyFirebaseIdService service);

    void inject(FridgeAdapter adapter);

    void inject(FoodShareFragment fragment);

    PrefsRepository prefsRepository();

    DataRepository dataManager();

    Context context();

    /*AnalyticsHandler analytics();*/

}
