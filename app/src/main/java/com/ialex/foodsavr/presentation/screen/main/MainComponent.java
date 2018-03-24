package com.ialex.foodsavr.presentation.screen.main;

import com.ialex.foodsavr.injection.AppComponent;
import com.ialex.foodsavr.injection.PerActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class)
public interface MainComponent {

    void inject(MainActivity activity);

}
