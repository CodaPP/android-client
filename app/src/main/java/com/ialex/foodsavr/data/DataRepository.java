package com.ialex.foodsavr.data;

import com.ialex.foodsavr.data.local.prefs.PrefsRepository;
import com.ialex.foodsavr.data.remote.Api;
import com.ialex.foodsavr.data.remote.response.AddFridgeItemResponse;
import com.ialex.foodsavr.data.remote.response.BaseResponse;
import com.ialex.foodsavr.data.remote.response.ProductsResponse;
import com.ialex.foodsavr.data.remote.response.RegisterResponse;
import com.ialex.foodsavr.presentation.screen.login.LoginCallback;
import com.ialex.foodsavr.presentation.screen.main.SignOutListener;
import com.ialex.foodsavr.presentation.screen.main.fragments.ProductListener;

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

                RegisterResponse resp = response.body();
                if (resp.status) {
                    Timber.d("Logged in");

                    prefsRepository.setName(resp.name);

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

    public void logout(final SignOutListener callback) {
        api.logout().enqueue(new Callback<AddFridgeItemResponse>() {
            @Override
            public void onResponse(Call<AddFridgeItemResponse> call, Response<AddFridgeItemResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    return;
                }

                prefsRepository.setUsername(null);
                prefsRepository.setPassword(null);

                callback.onSignOut();
            }

            @Override
            public void onFailure(Call<AddFridgeItemResponse> call, Throwable t) {

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

    public void getFridgeItems(final ProductListener callback) {
        api.getFridgeItems().enqueue(new Callback<ProductsResponse>() {
            @Override
            public void onResponse(Call<ProductsResponse> call, Response<ProductsResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    return;
                }

                ProductsResponse resp = response.body();

                if (!resp.status) {
                    callback.onError(resp.error);
                    return;
                }

                Timber.d("Got %d products", resp.items.size());
                callback.onReceiveFridgeItems(resp.items);
            }

            @Override
            public void onFailure(Call<ProductsResponse> call, Throwable t) {
                Timber.d(t, "rip");
                callback.onError("Retrofit failure callback");
            }
        });
    }

    public void addFridgeItem(String barcode, final ProductListener callback) {
        api.addFridgeItem(barcode, 1, null).enqueue(new Callback<AddFridgeItemResponse>() {
            @Override
            public void onResponse(Call<AddFridgeItemResponse> call, Response<AddFridgeItemResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    return;
                }

                if (response.body().status) {
                    Timber.d("Successfully added product");
                    callback.onProductAdded(response.body());
                } else {
                    Timber.d("Couldn't add product");
                    callback.onError(response.body().error);
                }
            }

            @Override
            public void onFailure(Call<AddFridgeItemResponse> call, Throwable t) {
                callback.onError("Retrofit callback failure");
            }
        });
    }

    public void updateItemInfo(String barcode, String manufacturer, String name) {
        api.updateItemInfo(barcode, manufacturer, name).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    return;
                }

                if (response.body().status) {
                    Timber.d("Successfully updated product info");
                } else {
                    Timber.d("Couldn't update product info");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });
    }

    public void setFirebaseRefreshToken(String token) {
        api.sendRefreshToken(token).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    return;
                }

                if (response.body().status) {
                    Timber.d("Successfully sent token");
                } else {
                    Timber.d("Couldn't send token");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });
    }

    public void donateItems(int id, int quantity, final ProductListener callback) {
        api.donateItems(id, quantity).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    return;
                }

                if (response.body().status) {
                    Timber.d("Items donated successfully");
                    callback.onProductShared();
                } else {
                    Timber.d("Couldn't donate items");
                    callback.onError(response.body().error);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                callback.onError("Retrofit failure callback");
            }
        });
    }
}
