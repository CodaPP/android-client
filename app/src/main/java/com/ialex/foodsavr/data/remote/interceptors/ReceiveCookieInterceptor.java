package com.ialex.foodsavr.data.remote.interceptors;

import android.support.annotation.NonNull;

import com.ialex.foodsavr.data.local.prefs.PrefsRepository;
import com.ialex.foodsavr.data.util.Constants;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by alex on 14.12.2015.
 */
public class ReceiveCookieInterceptor implements Interceptor {

    private final PrefsRepository prefsRepository;

    public ReceiveCookieInterceptor(PrefsRepository prefsRepository) {
        this.prefsRepository = prefsRepository;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = new HashSet<>();

            for (String header : originalResponse.headers("Set-Cookie")) {
                if(header.contains(Constants.LARAVEL_SESSION)) {
                    cookies.add(header);

                    prefsRepository.setLaravelToken(header);
                }
            }

            prefsRepository.saveCookies(cookies);
        }

        return originalResponse;
    }
}
