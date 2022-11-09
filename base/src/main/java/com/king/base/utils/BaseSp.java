package com.king.base.utils;

import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

public
abstract
class BaseSp {

    private transient Gson gson;

    public BaseSp() {
        gson = new Gson();
    }

    public void save(){
        MMKV.defaultMMKV().putString(get_sp_key(),gson.toJson(this));
    }

    public void clear(){
        MMKV.defaultMMKV().putString(get_sp_key(),"");
    }

    public Object get_sp_data(){
        String string = MMKV.defaultMMKV().getString(get_sp_key(), "");
        return gson.fromJson(string,getClass());
    }

    public Gson get_Gson() {
        return gson;
    }

    public String get_sp_key(){
        return this.getClass().getName().replaceAll(".","_");
    }
}
