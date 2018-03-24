package com.ialex.foodsavr.data;

import com.ialex.foodsavr.data.local.prefs.PrefsRepository;
import com.ialex.foodsavr.data.remote.Api;
import com.ialex.foodsavr.data.remote.response.BaseResponse;
import com.ialex.foodsavr.data.remote.response.RegisterResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

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

    public void register(String user, String email, String password) {
        api.register(user, email, password).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    return;
                }

                BaseResponse resp = response.body();
                if (resp.status) {
                    Timber.d("Register succes");

                    test();
                } else {
                    Timber.d("Couldn't register you... reason %s", resp.error);
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Timber.e(t, "Failed register");
            }
        });
    }

    public void login(String email, String password) {
        api.login(email, password).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    return;
                }

                BaseResponse resp = response.body();
                if (resp.status) {
                    Timber.d("Logged in");

                    test();
                } else {
                    Timber.d("Couldn't register you... reason %s", resp.error);
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {

            }
        });
    }

    public void test() {
        api.testLogin().enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    return;
                }

                BaseResponse resp = response.body();

                if (resp.status) {
                    Timber.d("You are loged in");
                } else {
                    Timber.d("Couldn't log in... reason %s", resp.error);
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Timber.d("Failed login test");
            }
        });
    }
}
