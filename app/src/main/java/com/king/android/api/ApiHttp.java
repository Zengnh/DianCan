package com.king.android.api;

import com.king.android.Constants;
import com.king.android.http.BaseRetrofit;

public
class ApiHttp {

    public static ApiService getApiService(){
        return new BaseRetrofit<ApiService>(ApiService.class, Constants.HOST).getService();
    }

}
