package com.example.francis.examinationsystem.util.net;

import com.example.francis.examinationsystem.global.Constants;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wzn on 2017/3/21.
 */

public class RetrofitHelper {
    /**
     * 普通http请求
     */
    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(OkHttpClientHelper.getClient())
                    .baseUrl(Constants.Project.baseUrl)
                    .build();
        }
        return retrofit;
    }




}
