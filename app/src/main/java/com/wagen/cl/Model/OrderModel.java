package com.wagen.cl.Model;

import java.util.ArrayList;

public class OrderModel {
    public int order_id = -1;
    public int car_id = -1;
    public int package_id = -1;
    public String order_date="";
    public String order_time = "";
    public int order_status = -1;
    public int duration_time = -1;
    public String total_price = "";
    public  int membership_id = -1;
    public  int order_type = -1; // 0" at workshop , 1: at home
    public String city = "";
    public String address = "";
    public Integer[] serviceselected_status = new Integer[]{0,0,0,0,0,0};

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    public void setPackage_id(int package_id) {
        this.package_id = package_id;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public void setDuration_time(int duration_time) {
        this.duration_time = duration_time;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public void setMembership_id(int membership_id) {
        this.membership_id = membership_id;
    }

    public void setOrder_type(int order_type) {
        this.order_type = order_type;
    }
}
