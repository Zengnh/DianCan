package com.king.android.model;

import java.io.Serializable;

public
class Address implements Serializable {
    /*
       {
      "address_id": "46",
      "uid": "124",
      "name": "啦啦啦",
      "tel": "155665",
      "address": "兔兔",
      "is_default": "0",
      "area_id": "59",
      "district_id": "57",
      "area_name": "壯圍鄉",
      "district_name": "宜蘭縣"
    }
     */
    private String address_id;
    private String uid;
    private String name;
    private String tel;
    private String address;
    private String is_default;
    private String area_id;
    private String district_id;
    private String area_name;
    private String district_name;

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIs_default() {
        return is_default;
    }

    public void setIs_default(String is_default) {
        this.is_default = is_default;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }
}
