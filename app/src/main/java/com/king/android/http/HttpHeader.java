package com.king.android.http;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * http请求头参数和数据
 */
public
class HttpHeader implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request token = chain.request().newBuilder()
//                .header("token", "")
                .build();
        return chain.proceed(token);
    }
}
