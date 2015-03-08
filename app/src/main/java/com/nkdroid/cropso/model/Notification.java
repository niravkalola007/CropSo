package com.nkdroid.cropso.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nirav kalola on 3/8/2015.
 */
public class Notification {

    @SerializedName("id")
    public String id;

    @SerializedName("title")
    public String title;

    @SerializedName("message")
    public String message;

    @SerializedName("created_date")
    public String created_date;
}
