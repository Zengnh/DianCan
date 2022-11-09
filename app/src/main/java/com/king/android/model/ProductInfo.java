package com.king.android.model;

import java.util.List;

public
class ProductInfo {
    private String product_id;
    private String product_name;
    private String shop_id;
    private String type_id;
    private String product_pic1;
    private String product_pic2;
    private String product_pic3;
    private String product_num;
    private String unit;
    private String spec1;
    private String spec2;
    private String spec3;
    private String price1;
    private String price2;
    private String price3;
    private String is_out; //0 內用 1 外帶 2 兩種都可以
    private String is_sign; //是否招牌1是0否
    private String is_special_price;//是否特价 1是 0否
    private String start;
    private String end;
    private Param param; //甜度和冰度属性

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

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getProduct_pic1() {
        return product_pic1;
    }

    public void setProduct_pic1(String product_pic1) {
        this.product_pic1 = product_pic1;
    }

    public String getProduct_pic2() {
        return product_pic2;
    }

    public void setProduct_pic2(String product_pic2) {
        this.product_pic2 = product_pic2;
    }

    public String getProduct_pic3() {
        return product_pic3;
    }

    public void setProduct_pic3(String product_pic3) {
        this.product_pic3 = product_pic3;
    }

    public String getProduct_num() {
        return product_num;
    }

    public void setProduct_num(String product_num) {
        this.product_num = product_num;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSpec1() {
        return spec1;
    }

    public void setSpec1(String spec1) {
        this.spec1 = spec1;
    }

    public String getSpec2() {
        return spec2;
    }

    public void setSpec2(String spec2) {
        this.spec2 = spec2;
    }

    public String getSpec3() {
        return spec3;
    }

    public void setSpec3(String spec3) {
        this.spec3 = spec3;
    }

    public String getPrice1() {
        return price1;
    }

    public void setPrice1(String price1) {
        this.price1 = price1;
    }

    public String getPrice2() {
        return price2;
    }

    public void setPrice2(String price2) {
        this.price2 = price2;
    }

    public String getPrice3() {
        return price3;
    }

    public void setPrice3(String price3) {
        this.price3 = price3;
    }

    public String getIs_out() {
        return is_out;
    }

    public void setIs_out(String is_out) {
        this.is_out = is_out;
    }

    public String getIs_sign() {
        return is_sign;
    }

    public void setIs_sign(String is_sign) {
        this.is_sign = is_sign;
    }

    public String getIs_special_price() {
        return is_special_price;
    }

    public void setIs_special_price(String is_special_price) {
        this.is_special_price = is_special_price;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Param getParam() {
        return param;
    }

    public void setParam(Param param) {
        this.param = param;
    }

    public static class Param{
        private List<String> taste; //甜度
        private List<String> ice; //冰度
        private List<String> pungency; //冰度
        private String taste_open; //是否需要甜度  0 隐藏 1 显示
        private String ice_open; //是否需要冰度  0 隐藏 1 显示
        private String pungency_open; //是否需要冰度  0 隐藏 1 显示
        private String cold_open;
        private String cold_cooler;
        private String cold_hot;

        public String getCold_open() {
            return cold_open;
        }

        public void setCold_open(String cold_open) {
            this.cold_open = cold_open;
        }

        public String getCold_cooler() {
            return cold_cooler;
        }

        public void setCold_cooler(String cold_cooler) {
            this.cold_cooler = cold_cooler;
        }

        public String getCold_hot() {
            return cold_hot;
        }

        public void setCold_hot(String cold_hot) {
            this.cold_hot = cold_hot;
        }

        public List<String> getTaste() {
            return taste;
        }

        public void setTaste(List<String> taste) {
            this.taste = taste;
        }

        public List<String> getIce() {
            return ice;
        }

        public void setIce(List<String> ice) {
            this.ice = ice;
        }

        public String getTaste_open() {
            return taste_open;
        }

        public void setTaste_open(String taste_open) {
            this.taste_open = taste_open;
        }

        public String getIce_open() {
            return ice_open;
        }

        public void setIce_open(String ice_open) {
            this.ice_open = ice_open;
        }

        public List<String> getPungency() {
            return pungency;
        }

        public void setPungency(List<String> pungency) {
            this.pungency = pungency;
        }

        public String getPungency_open() {
            return pungency_open;
        }

        public void setPungency_open(String pungency_open) {
            this.pungency_open = pungency_open;
        }
    }
}
