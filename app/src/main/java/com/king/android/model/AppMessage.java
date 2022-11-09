package com.king.android.model;

public class AppMessage {
    private int type;
    private String title;
    private String content;
    private String time;

    public AppMessage(int type,String title, String time,String content){
        setContent(content);
        setTime(time);
        setTitle(title);
        setType(type);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
