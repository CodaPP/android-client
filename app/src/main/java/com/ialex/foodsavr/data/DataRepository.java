package com.ialex.foodsavr.data;

import com.ialex.foodsavr.data.local.prefs.PrefsRepository;
import com.ialex.foodsavr.data.remote.Api;
import com.ialex.foodsavr.data.remote.response.BaseResponse;
import com.ialex.foodsavr.data.remote.response.ProductsResponse;
import com.ialex.foodsavr.data.remote.response.RegisterResponse;
import com.ialex.foodsavr.presentation.screen.login.LoginCallback;

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

    public void register(String user, String email, String password, final LoginCallback callback) {
        api.register(user, email, password).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    callback.onLoginError("Unknown error!");
                    return;
                }

                BaseResponse resp = response.body();
                if (resp.status) {
                    Timber.d("Register succes");

                    callback.onLoginSuccess();
                } else {
                    Timber.d("Couldn't register you... reason %s", resp.error);
                    callback.onLoginError(resp.error);
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Timber.e(t, "Failed register");
                callback.onLoginError("Retrofit failed callback");
            }
        });
    }

    public void login(String email, String password, final LoginCallback callback) {
        api.login(email, password).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    callback.onLoginError("Unknown error!");
                    return;
                }

                BaseResponse resp = response.body();
                if (resp.status) {
                    Timber.d("Logged in");

                    callback.onLoginSuccess();
                } else {
                    callback.onLoginError(resp.error);
                    Timber.d("Couldn't register you... reason %s", resp.error);
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                callback.onLoginError("Retrofit failed callback");
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

    public void getFridgeItems() {
        api.getFridgeItems().enqueue(new Callback<ProductsResponse>() {
            @Override
            public void onResponse(Call<ProductsResponse> call, Response<ProductsResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    return;
                }

                ProductsResponse resp = response.body();
                Timber.d("Got %d products", resp.items.size());
            }

            @Override
            public void onFailure(Call<ProductsResponse> call, Throwable t) {
                Timber.d(t, "rip");
            }
        });
    }
}
