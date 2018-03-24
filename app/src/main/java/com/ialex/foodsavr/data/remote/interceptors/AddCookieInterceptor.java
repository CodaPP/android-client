package com.ialex.foodsavr.data.remote.interceptors;

import android.support.annotation.NonNull;
import android.util.Log;

import com.ialex.foodsavr.data.local.prefs.PrefsRepository;
import com.ialex.foodsavr.data.util.Constants;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

/**
 * Created by alex on 14.12.2015.
 */
public class AddCookieInterceptor implements Interceptor {

    private final PrefsRepository prefsRepository;

    public AddCookieInterceptor(PrefsRepository prefsRepository) {
        this.prefsRepository = prefsRepository;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        HashSet<String> cookies = (HashSet) prefsRepository.getCookies();

        if(cookies != null) {
            for (String cookie : cookies) {
                if(cookie.contains(Constants.LARAVEL_SESSION)) {
                    Map<String, String> temp = splitCookie(cookie);

                    String token = temp.get(Constants.LARAVEL_SESSION);

                    builder.addHeader("Cookie", Constants.LARAVEL_SESSION + "=" + token);
                    Timber.d("Adding Header: " + Constants.LARAVEL_SESSION + "=" + token); // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
                }
            }
        } else {
            Log.d("interceptor", "Null iac_session, not adding this cookie");
        }

        return chain.proceed(builder.build());
    }

    /**
     *
     * @param cookie string
     * @return the cookie map;
     */

    private Map<String, String> splitCookie(String cookie) { //TODO build Cookie class and store this shit
        Map<String, String> prajitura = new HashMap<>();

        String[] parts = cookie.split(";");
        for(String part : parts) {
            String[] args = part.split("=");

            if (args.length > 1) {
                prajitura.put(args[0].trim(), args[1].trim());
            }
        }
        return prajitura;
    }
}
