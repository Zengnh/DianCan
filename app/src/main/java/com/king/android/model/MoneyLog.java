package com.king.android.model;

import java.util.List;

public
class MoneyLog {
    private String money;
    private List<Data> list;

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
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
        "id": "6",
        "order_no": "2022030209475357525",
        "user_id": "124",
        "user_name": "0965391369",
        "type": "0",
        "money": "20",
        "status": "0",
        "remaining": "0",
        "time": "1646185673"
      }
         */
        private String id;
        private String order_no;
        private String user_id;
        private String user_name;
        private String type;
        private String money;
        private String status;
        private String remaining;
        private long time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrder_no() {
            return order_no;
        }

        public void setOrder_no(String order_no) {
            this.order_no = order_no;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getRemaining() {
            return remaining;
        }

        public void setRemaining(String remaining) {
            this.remaining = remaining;
        }

        public long getTime() {
            return time * 1000;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }
}
