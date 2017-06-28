package com.mobymagic.bakingapp.data.remote;

import android.support.annotation.NonNull;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit sRetrofitClient = null;

    public static Retrofit getRetrofitClient(@NonNull String baseUrl) {
        if (sRetrofitClient == null) {
            sRetrofitClient = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }

        return sRetrofitClient;
    }
}
