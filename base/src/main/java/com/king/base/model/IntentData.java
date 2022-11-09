package com.king.base.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.king.base.utils.type.TypeBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 封装activity值之间传值
 */
public
class IntentData {
    private JSONArray list;
    private JSONObject map;
    private transient Context context;
    private transient Class<? extends Activity> activityClass;
    private transient Fragment fragment;

    public IntentData(Context context, Class<? extends Activity> activityClass) {
        this();
        this.context = context;
        this.activityClass = activityClass;
    }

    public IntentData(Fragment fragment) {
        this();
        this.fragment = fragment;
    }

    public IntentData() {
        list = new JSONArray();
        map = new JSONObject();
    }

    public void start() {
        Intent it = new Intent(context, activityClass);
        Gson gson = new Gson();
        String data = gson.toJson(this);
        it.putExtra("data",data);
        context.startActivity(it);
    }

    public Fragment getFragment(){
        Gson gson = new Gson();
        Bundle bundle = new Bundle();
        bundle.putString("data",gson.toJson(this));
        fragment.setArguments(bundle);
        return fragment;
    }

    public IntentData add(Object value) {
        list.put(value);
        return this;
    }

    public IntentData add(String key, Object value) {
        try {
            map.put(key, value);
        } catch (Exception e) {
        }
        return this;
    }

    public <D> D getModel(int index,Class<D> d){
        String string = getString(index);
        if (d == String.class){
            return (D) string;
        }
       return new Gson().fromJson(string,d);
    }

    public <D> List<D> getModels(int index,Class<D> d){
        Type type = TypeBuilder
                .newInstance(List.class)
                .beginSubType(d)
                .endSubType()
                .build();
        String string = getString(index);
        return new Gson().fromJson(string,type);
    }


    public <D> D getModel(String key,Class<D> d){
        String string = getString(key);
        return new Gson().fromJson(string,d);
    }

    public <D> List<D> getModels(String key,Class<D> d){
        Type type = TypeBuilder
                .newInstance(List.class)
                .beginSubType(d)
                .endSubType()
                .build();
        String string = getString(key);
        return new Gson().fromJson(string,type);
    }

    public String getString(int index) {
        if (index < list.length()) {
            try {
                return list.getString(index);
            } catch (Exception e) {
            }
        }
        return "";
    }

    public int getInt(int index) {
        if (index < list.length()) {
            try {
                return list.getInt(index);
            } catch (Exception e) {
            }
        }
        return 0;
    }

    public long getLong(int index) {
        if (index < list.length()) {
            try {
                return list.getLong(index);
            } catch (Exception e) {
            }
        }
        return 0;
    }

    public double getDouble(int index) {
        if (index < list.length()) {
            try {
                return list.getDouble(index);
            } catch (Exception e) {
            }
        }
        return 0;
    }

    public String getString(String key) {
        try {
            return map.getString(key);
        } catch (Exception e) {
        }
        return "";
    }

    public int getInt(String key) {
        try {
            return map.getInt(key);
        } catch (Exception e) {
        }
        return 0;
    }

    public long getLong(String key) {
        try {
            return map.getLong(key);
        } catch (Exception e) {
        }
        return 0;
    }

    public double getDouble(String key) {
        try {
            return map.getDouble(key);
        } catch (Exception e) {
        }
        return 0;
    }

}
