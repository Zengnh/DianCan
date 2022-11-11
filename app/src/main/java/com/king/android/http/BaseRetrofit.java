package com.king.android.http;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.king.android.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseRetrofit<T> {
    private static int DEFAULT_TIME = 25;
    private String url;
    private Retrofit retrofit;
    private Class<T> tClass;
    private T service;

    public BaseRetrofit(Class<T> tClass, String url) {
        this.tClass = tClass;
        this.url = url;
        initRetrofit(tClass);
    }

    private void initRetrofit(Class<T> tClass) {
        //调度器
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(10);
        dispatcher.setMaxRequestsPerHost(10);

        HttpLoggingInterceptor httpLog = null;
//        if (BuildConfig.DEBUG) {
        httpLog = new HttpLoggingInterceptor(new HttpLog());
        httpLog.setLevel(HttpLoggingInterceptor.Level.BODY);
//        }

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(DEFAULT_TIME, TimeUnit.SECONDS)//设置读取超时时间
                .connectTimeout(DEFAULT_TIME, TimeUnit.SECONDS)//设置请求超时时间
                .writeTimeout(DEFAULT_TIME, TimeUnit.SECONDS);//设置写入超时时间
//        if (BuildConfig.DEBUG) {
        builder.addInterceptor(httpLog);//添加打印拦截器
//        }
        builder.dispatcher(dispatcher);
        OkHttpClient okHttpClient = builder.build();

        //创建retrofit实例
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .build();
        //创建请求接口实例
        service = retrofit.create(tClass);
    }

    public T getService() {
        if (null == service) {
            service = retrofit.create(tClass);
        }
        return service;
    }
}