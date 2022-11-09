package com.king.android.model;

import java.util.List;

public
class ShopInfo {
    private Shops info;
    private List<Cat> cat_lists;
    private List<Product> products;

    public Shops getInfo() {
        return info;
    }

    public void setInfo(Shops info) {
        this.info = info;
    }

    public List<Cat> getCat_lists() {
        return cat_lists;
    }

    public void setCat_lists(List<Cat> cat_lists) {
        this.cat_lists = cat_lists;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public static class Cat{
        private String type_id;
        private String type_name;

        public String getType_id() {
            return type_id;
        }

        public void setType_id(String type_id) {
            this.type_id = type_id;
        }

        public String getType_name() {
            return type_name;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
        }
    }
    public static class Product{
        private String product_id;
        private String product_name;
        private String shop_id;
        private String type_id;
        private String product_num;
        private String unit;
        private String img;
        private String spec1;
        private String spec2;
        private String spec3;
        private String price1;
        private String price2;
        private String price3;

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

        public String getProduct_num() {
            return String.format("剩下%s杯",product_num);
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

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
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
            return price1+"元";
        }

        public void setPrice1(String price1) {
            this.price1 = price1;
        }

        public String getPrice2() {
            return price2+"元";
        }

        public void setPrice2(String price2) {
            this.price2 = price2;
        }

        public String getPrice3() {
            return price3+"元";
        }

        public void setPrice3(String price3) {
            this.price3 = price3;
        }
    }
}
