package com.ialex.foodsavr.data;

import com.ialex.foodsavr.data.local.prefs.PrefsRepository;
import com.ialex.foodsavr.data.remote.Api;

/**
 * Created by ialex on 15.02.2017.
 */

public class DataRepository {

    private Api api;
    private PrefsRepository prefsRepository;

    public DataRepository(Api api, PrefsRepository prefsRepository) {
        this.api = api;
        this.prefsRepository = prefsRepository;
    }
}
