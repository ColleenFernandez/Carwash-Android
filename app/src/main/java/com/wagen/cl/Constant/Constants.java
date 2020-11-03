package com.wagen.cl.Constant;

import com.wagen.cl.Model.OrderModel;
import com.wagen.cl.Model.UserModel;

public class Constants {
    public static final int PICK_FROM_CAMERA = 100;
    public static final int PICK_FROM_ALBUM = 101;
    public static final int PROFILE_IMAGE_SIZE1 = 256;
    public static final int PROFILE_IMAGE_SIZE = 256;
    public static String key ="";

    public static final String BASE_URL = "http://192.168.0.33:8080/CarwashBackend/index.php/Api/";
    //public static final String BASE_URL = "https://wagen.cl/admin/index.php/Api/";

    public static UserModel userModel= new UserModel();
    public static OrderModel orderModel = new OrderModel();
    public static int selectedvehicle = -1;
    public static String verifycode = "-1";
    public static String email = "-1";
    public static int frompagestatus_fororderlist = 0; // 0: from main, 1: from rating
}
