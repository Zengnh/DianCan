package com.king.android.model;

import java.util.List;


public class StoreCarList {
    String shop_name;//", "bbb");
    String shop_id;//", "aaa");
    List<Cart> list;

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public List<Cart> getList() {
        return list;
    }

    public void setList(List<Cart> list) {
        this.list = list;
    }
}
