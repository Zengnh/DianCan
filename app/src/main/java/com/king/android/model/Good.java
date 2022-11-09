package com.king.android.model;

public
class Good {
    private String id;
    private String order_id;
    private String product_id;
    private String product_name;
    private String product_image;
    private String price;
    private String product_num;
    private String spec;
    /*
       {
                    "id": "1203",
                    "order_id": "1104",
                    "product_id": "161",
                    "product_name": "拿鐵",
                    "product_image": "",
                    "price": "250",
                    "product_num": "1",
                    "spec": "熱飲/無糖/去冰"
                }
     */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
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

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProduct_num() {
        return product_num;
    }

    public void setProduct_num(String product_num) {
        this.product_num = product_num;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }
}
