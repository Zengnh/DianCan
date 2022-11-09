package com.king.android.model;

import java.util.List;

public
class Floor {
    private String floor;
    private List<FloorNumber> list;

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public List<FloorNumber> getList() {
        return list;
    }

    public void setList(List<FloorNumber> list) {
        this.list = list;
    }

    public static class FloorNumber{
        private String table_number;
        private String status;

        public String getTable_number() {
            return table_number;
        }

        public void setTable_number(String table_number) {
            this.table_number = table_number;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
