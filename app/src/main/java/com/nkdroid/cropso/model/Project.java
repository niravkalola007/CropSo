package com.nkdroid.cropso.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nirav kalola on 3/8/2015.
 */
public class Project {

    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("abstract")
    public String abstractvalue;
    @SerializedName("modules")
    public String modules;
    @SerializedName("functions")
    public String functions;
    @SerializedName("programming_languages")
    public String programming_languages;
    @SerializedName("technology_id")
    public String technology_id;

    @SerializedName("front_end")
    public String front_end;
    @SerializedName("back_end")
    public String back_end;
    @SerializedName("start_date")
    public String start_date;
    @SerializedName("deadline_date")
    public String deadline_date;
    @SerializedName("progress")
    public String progress;
}
