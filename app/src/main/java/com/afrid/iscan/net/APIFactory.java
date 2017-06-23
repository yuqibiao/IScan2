package com.afrid.iscan.net;

import com.yyyu.baselibrary.utils.MyLog;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 功能：网络请求的封装
 *
 * @author yyyu
 * @version 1.0
 * @date 2017/5/8
 */

public class APIFactory {

    public static final String BASE_URL = "http://news-at.zhihu.com/api/4/";

    private static final int DEFAULT_TIMEOUT = 5;
    private Retrofit.Builder builder;

    private APIFactory() {

        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                MyLog.i("APIFactory: OkHttpClient", "OkHttpMessage:" + message);
            }
        });
        loggingInterceptor.setLevel(level);
        OkHttpClient.Builder httpClientBuild = new OkHttpClient.Builder();
        httpClientBuild.addInterceptor(loggingInterceptor);
        httpClientBuild.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder = new Retrofit.Builder()
                .client(httpClientBuild.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
    }

    public static APIFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final APIFactory INSTANCE = new APIFactory();
    }

    public UrlApi createUrlApi() {
        Retrofit retrofit = builder.baseUrl(BASE_URL).build();
        UrlApi urlApi = retrofit.create(UrlApi.class);
        return urlApi;
    }

}