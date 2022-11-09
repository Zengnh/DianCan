package com.king.android.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.List;

public
class Order {
    private String order_id;
    private String user_id;
    private String shop_id;
    private String shop_name;
    private String accept_name;
    private String address;
    private String mobile;
    private String status;
    private String send_status;
    private String remark;
    private String real_amount;
    private String ship_price;
    private String payment;
    private String create_time;
    private String user_comment;
    private String star_num;
    private String group_no;
    private String group_id;
    private String is_outside;
    private String deliveryuser_name;
    private String deliveryuser_phone;
    private String deliveryuser_time;
    private List<Good> order_goods;

    public String getIs_outside() {
        if ("0".equals(is_outside)){
            return "否";
        }
        return "是";
    }

    public void setIs_outside(String is_outside) {
        this.is_outside = is_outside;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getAccept_name() {
        return accept_name;
    }

    public void setAccept_name(String accept_name) {
        this.accept_name = accept_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSend_status() {
        //-1商家拒绝0未接单1已接单2已送达3完成订单4已评价
        if ("-1".equals(send_status)){
            return "商家拒絕";
        }else  if ("0".equals(send_status)){
            return "未接單";
        } if ("1".equals(send_status)){
            return "已接單";
        } if ("2".equals(send_status)){
            return "已送達";
        } if ("3".equals(send_status)){
            return "已完成";
        } if ("4".equals(send_status)){
            return "已評價";
        }
        return "未知";
    }

    public String getSendStatus(){
        return send_status;
    }

    public void setSend_status(String send_status) {
        this.send_status = send_status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReal_amount() {
        return real_amount;
    }

    public void setReal_amount(String real_amount) {
        this.real_amount = real_amount;
    }

    public String getShip_price() {
        return ship_price;
    }
    public String getShipPrice() {
        return ship_price+"元";
    }

    public void setShip_price(String ship_price) {
        this.ship_price = ship_price;
    }

    public String getDeliveryuser_name() {
        return deliveryuser_name;
    }

    public void setDeliveryuser_name(String deliveryuser_name) {
        this.deliveryuser_name = deliveryuser_name;
    }

    public String getDeliveryuser_phone() {
        return deliveryuser_phone;
    }

    public void setDeliveryuser_phone(String deliveryuser_phone) {
        this.deliveryuser_phone = deliveryuser_phone;
    }

    public String getDeliveryuser_time() {
        return deliveryuser_time;
    }

    public void setDeliveryuser_time(String deliveryuser_time) {
        this.deliveryuser_time = deliveryuser_time;
    }

    public String getPayment() {
        if ("1".equals(payment)){
            return "現金";
        }else if ("2".equals(payment)){
            return "電子支付";
        }
        return "未知";
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUser_comment() {
        if (user_comment == null){
            user_comment = "";
        }
        return user_comment;
    }

    public String getShopNum(){
        if (order_goods != null && order_goods.size() > 0){
            return "X"+order_goods.get(0).getProduct_num();
        }
        return "X0";
    }

    public String getSpec(){
        if (order_goods != null && order_goods.size() > 0){
            return order_goods.get(0).getSpec();
        }
        return "";
    }

    public String getProductImage(){
        if (order_goods != null && order_goods.size() > 0){
            return order_goods.get(0).getProduct_image();
        }
        return "";
    }

    public String getProductName(){
        if (order_goods != null && order_goods.size() > 0){
            return order_goods.get(0).getProduct_name();
        }
        return "";
    }

    public String getOrdersPrice(){
        if (order_goods != null){
            BigDecimal bd = new BigDecimal("0");
            for (int i = 0; i < order_goods.size(); i++) {
                Good good = order_goods.get(i);
                bd = bd.add(new BigDecimal(good.getPrice()).multiply(new BigDecimal(good.getProduct_num())));
            }
            return bd.toString();
        }
        return "0";
    }

    /**
     * 获取第一个商品的单价
     * @return
     */
    public String getFirstGoodPrice(){
        if (order_goods != null && order_goods.size() > 0){
            return order_goods.get(0).getPrice();
        }
        return "0";
    }

    public String getPriceDetails(){
//        if (order_goods != null && order_goods.size() > 0){
//            StringBuffer sb  =new StringBuffer();
//            for (Good order_good : order_goods) {
//                order_good.getProduct_name()
//            }
//
//        }
        return getReal_amount()+"元";
    }

    public void setUser_comment(String user_comment) {
        this.user_comment = user_comment;
    }

    public String getStar_num() {
        return star_num;
    }

    public void setStar_num(String star_num) {
        this.star_num = star_num;
    }

    public String getGroup_no() {
        return group_no;
    }

    public void setGroup_no(String group_no) {
        this.group_no = group_no;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public List<Good> getOrder_goods() {
        return order_goods;
    }

    public void setOrder_goods(List<Good> order_goods) {
        this.order_goods = order_goods;
    }
    /*
    {
            "order_id": "1104",
            "user_id": "124",
            "shop_id": "80",
            "shop_name": "煌哥早餐店",
            "accept_name": "bbg",
            "address": "連江縣北竿鄉yyyu",
            "mobile": "366333",
            "status": "1",
            "send_status": "0",
            "remark": "",
            "real_amount": "312",
            "ship_price": "60",
            "payment": "2",
            "create_time": "2022-03-07 19:55:45",
            "user_comment": "",
            "star_num": "0",
            "group_no": "0",
            "group_id": "0",
            "order_goods": [

            ]
        }
     */


}
