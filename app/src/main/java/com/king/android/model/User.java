package com.king.android.model;

import android.text.TextUtils;

import com.king.base.utils.BaseSp;

import java.math.BigDecimal;

public
class User extends BaseSp {
    /*
    {
    "uid": "123",
    "user_name": "0965391369",
    "name": "Test1",
    "cover": "",
    "sex": "0",
    "mobile": "0965391369",
    "address": "",
    "reg_time": "2022-02-15 16:20:30",
    "password": "123456",
    "birthday": "",
    "money": "0",
    "bazaar_id": "0",
    "points": "0",
    "nick_name": "Test1",
    "city_id": "0",
    "area_id": "0",
    "access_token": "bf40UfRh1xFGkRckCFXYCHZaGjdKtAuapXAJXanmlf4"
  }
     */

    public static User getInstance() {
        User u = new User();
        User u1 = (User) u.get_sp_data();
        if (u1 == null) {
            u1 = new User();
        }
        return u1;
    }

    private String uid;
    private String user_name;
    private String name;
    private String cover;
    private String sex;
    private String mobile;
    private String address;
    private String reg_time;
    private String password;
    private String birthday;
    private String money;
    private String bazaar_id;
    private String points;
    private String nick_name;
    private String city_id;
    private String area_id;
    private String access_token;
    private String score;
    private String recommend_code;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

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

    public String getCover() {
//        if (cover.startsWith("http://")){
//            System.out.println(cover);
//            return cover.replaceFirst("http://","https://");
//        }
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReg_time() {
        return reg_time;
    }

    public void setReg_time(String reg_time) {
        this.reg_time = reg_time;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getBazaar_id() {
        return bazaar_id;
    }

    public void setBazaar_id(String bazaar_id) {
        this.bazaar_id = bazaar_id;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getAccess_token() {
        if (access_token == null) {
            access_token = "";
        }
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRecommend_code() {
        return recommend_code;
    }

    public void setRecommend_code(String recommend_code) {
        this.recommend_code = recommend_code;
    }

    public String getScore() {
        if(TextUtils.isEmpty(score)){
            score="0";
        }
        return new BigDecimal(score).stripTrailingZeros().toPlainString();
//        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
