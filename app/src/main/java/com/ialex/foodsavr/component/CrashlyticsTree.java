package com.ialex.foodsavr.component;

import android.util.Log;

import timber.log.Timber;

public class CrashlyticsTree extends Timber.Tree {

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return;
        }

        //Crashlytics.log(message);

        if (t != null) {
            if (priority == Log.ERROR) {
                //Crashlytics.logException(t);
            }
        }
    }
}