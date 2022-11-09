package com.king.android.model;

import java.math.BigDecimal;

public
class Charge {
    /*
    {
    "order_no": "2022030513525454485",
    "user_id": "124",
    "user_name": "0965391369",
    "type": 0,
    "money": 100,
    "status": 0,
    "time": 1646459574,
    "id": 7
  }
     */
    private String order_no;
    private String user_id;
    private String user_name;
    private int type;
    private BigDecimal money;
    private int status;
    private long time;
    private long id;
    private long order_id;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }
}
