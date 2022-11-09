package com.king.android.model;

import android.text.TextUtils;

public
class Cart {
    private String id;
    private String product_id;
    private String product_name;
    private String product_img;
    private String spec_name;
    private String taste;
    private String pungency;
    private String cold;
    private String ice;
    private String remark;
    private String send_time;
    private String num;
    private String price;
    private String total;
    private String is_outside; //是否外帶
    private String pack_price; // 打包费
    private String send_price; //运费
    private String shop_name; //店铺名称
    private String shop_id; //店铺名称
    private int index;

    public String getIs_outside() {
        return is_outside;
    }

    public void setIs_outside(String is_outside) {
        this.is_outside = is_outside;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getShop_name() {
        return shop_name;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getSpec_name() {
        return spec_name;
    }

    public void setSpec_name(String spec_name) {
        this.spec_name = spec_name;
    }

    public String getTaste() {
        if (taste == null){
            return "";
        }
        return taste;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }

    public String getIce() {
        if (ice == null){
            return "";
        }
        return ice;
    }

    public void setIce(String ice) {
        this.ice = ice;
    }

    public String getRemark() {
        if (remark == null){
            return "";
        }
        return remark;
    }

    public String getPungency() {
        return pungency;
    }

    public void setPungency(String pungency) {
        this.pungency = pungency;
    }

    public String getCold() {
        return cold;
    }

    public void setCold(String cold) {
        this.cold = cold;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public String getNum() {
        if (TextUtils.isEmpty(num)){
            return "0";
        }
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotal() {
        return total + "元";
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPack_price() {
        return pack_price;
    }

    public void setPack_price(String pack_price) {
        this.pack_price = pack_price;
    }

    public String getSend_price() {
        return send_price;
    }

    public void setSend_price(String send_price) {
        this.send_price = send_price;
    }

    public String getProduct_img() {
        return product_img;
    }

    public void setProduct_img(String product_img) {
        this.product_img = product_img;
    }
}
