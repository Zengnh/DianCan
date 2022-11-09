package com.king.android.http;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * http请求日志输出
 * 发起请求输出
 *  url
 *  header
 * 接口返回输出
 *  url
 *  数据
 */
public
class HttpLog implements HttpLoggingInterceptor.Logger {
    private static final String TAG = "HttpLog";
    @Override
    public void log(String message) {
        message = message.trim();
        if (message.contains("code") && message.contains("data")){
            try {
                message = new JSONObject(message).toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG, message);

    }
}
