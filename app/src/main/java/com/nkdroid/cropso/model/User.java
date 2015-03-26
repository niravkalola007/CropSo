package com.nkdroid.cropso.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nirav kalola on 3/8/2015.
 */
public class User {

    @SerializedName("message")
    public String message;
    @SerializedName("id")
    public String id;
    @SerializedName("username")
    public String username;
    @SerializedName("fname")
    public String fname;
    @SerializedName("lname")
    public String lname;
    @SerializedName("mobile")
    public String mobile;
    @SerializedName("registration_type")
    public String registration_type;
    @SerializedName("notification_id")
    public String notification_id;


}
