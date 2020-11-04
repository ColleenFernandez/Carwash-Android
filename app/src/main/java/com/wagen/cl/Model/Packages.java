package com.wagen.cl.Model;

import java.util.ArrayList;

public class Packages {
    public int package_id = -1;
    public String package_name = "";
    public String package_time = "";
    public String package_description = "";
    public String package_available_for_home = "No";
    public String selected_car_price = ""; // can be workshop price or home price
    public boolean selected_status = false;

    public ArrayList<PackagePricesModel> packagePricesModels = new ArrayList<>();
}
