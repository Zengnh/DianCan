package com.king.android.model;

import java.math.BigDecimal;
import java.util.List;

public
class Score {
    /*

     */

    private String score;
    private List<Data> list;

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public List<Data> getList() {
        return list;
    }

    public void setList(List<Data> list) {
        this.list = list;
    }

    public static class Data{
        /*
        {
        "id": "4",
        "type": "0",
        "model": "0",
        "pid": "124",
        "score": "1.0",
        "time": "2022-03-09 13:25:29",
        "order_id": "1115",
        "from_uid": "124",
        "my_order": 1,
        "order_info": {
          "order_id": "1115",
          "shop_name": "煌哥早餐店",
          "order_goods": [

          ]
        }
      }
         */
        private String id;
        private String type;
        private String model;
        private String pid;
        private String score;
        private String time;
        private String order_id;
        private String from_uid;
        private String my_order;
        private OrderInfo order_info;
        private UserInfo user_info;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getScore() {
            return new BigDecimal(score).stripTrailingZeros().toPlainString() +"點";
        }

        public String getScoreName(){
            return getModelType()+"點數";
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getTime() {
            return getModelType()+"時間:"+time;
        }

        public String getFrom(){
            if ("0".equals(model)){
                return order_info.shop_name;
            }else if ("2".equals(model) || "3".equals(model)){
                return "來源:"+user_info.user_name+"  "+user_info.name;
            }else if ("1".equals(model)){
                return "注冊赠送";
            }
            return "未知";
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getFrom_uid() {
            return from_uid;
        }

        public void setFrom_uid(String from_uid) {
            this.from_uid = from_uid;
        }

        public String getMy_order() {
            return my_order;
        }

        public void setMy_order(String my_order) {
            this.my_order = my_order;
        }

        public OrderInfo getOrder_info() {
            return order_info;
        }

        public void setOrder_info(OrderInfo order_info) {
            this.order_info = order_info;
        }

        public UserInfo getUser_info() {
            return user_info;
        }

        public void setUser_info(UserInfo user_info) {
            this.user_info = user_info;
        }

        public String getModelType(){
        //0-訂單 1-註冊 2-受贈 3-转让
            if ("0".equals(model)){
                return "訂單";
            }else if ("1".equals(model)){
                return "註冊";
            }else if ("2".equals(model)){
                return "受贈";
            }else if ("3".equals(model)){
                return "转让";
            }else {
                return "未知";
            }
        }
    }

    public static class UserInfo{
        private String user_name;
        private String name;

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class OrderInfo{
        /*   {
              "product_name": "拿鐵",
              "price": "4",
              "product_num": "10"
            }

         */
        private String order_id;
        private String shop_name;
        private List<OrderGoods> order_goods;

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getShop_name() {
            return shop_name;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }

        public List<OrderGoods> getOrder_goods() {
            return order_goods;
        }

        public void setOrder_goods(List<OrderGoods> order_goods) {
            this.order_goods = order_goods;
        }
    }

    public static class OrderGoods{
        /*   {
              "product_name": "拿鐵",
              "price": "4",
              "product_num": "10"
            }

         */
        private String product_name;
        private String price;
        private String product_num;

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
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
    }
}
