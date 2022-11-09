package com.king.android.api;

public
class Result<D> {
    private static final String success_code = "200";

    private String code;
    private String msg;
    private D data;

    public boolean isSuccess(){
        return success_code.equals(code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }
}
