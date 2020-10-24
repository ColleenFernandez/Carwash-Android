package com.wagen.cl.Model;

public class UserModel {
    public int user_id=-1;
    public String first_name = "";
    public String last_name = "";
    public String email = "";
    public String phonenumber = "";
    public String password = "";
    public String photo = "";
    public int account_type = 0; // 0 normal, 1: google, 2: facebook
    public String social_id = "0";
    public String membership_last_renew = "-1";  // last renew date
    public int membership_id = -1;
    public int membership_count = 0;


}
