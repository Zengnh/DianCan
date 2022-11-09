package com.king.android.model;

import java.util.List;

public
class City {
    private String area_id;
    private String area_name;
    private String level;
    private String parent;
    private List<City> area;

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public List<City> getArea() {
        return area;
    }

    public void setArea(List<City> area) {
        this.area = area;
    }
}
