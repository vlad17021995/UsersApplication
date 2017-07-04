package com.vlad17021995m.android.usersapplication.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by qwerty on 04.07.2017.
 */

public class Mail {
    @SerializedName("email")
    @Expose public String email;
    @SerializedName("did_you_mean")
    @Expose public String did_you_mean;
    @SerializedName("user")
    @Expose public String user;
    @SerializedName("domain")
    @Expose public String domain;
    @SerializedName("format_valid")
    @Expose public boolean format_valid;
    @SerializedName("mx_found")
    @Expose public boolean mx_found;
    @SerializedName("smtp_check")
    @Expose public boolean smtp_check;
    @SerializedName("catch_all")
    @Expose public boolean catch_all;
    @SerializedName("role")
    @Expose public boolean role;
    @SerializedName("disposable")
    @Expose public boolean disposable;
    @SerializedName("free")
    @Expose public boolean free;
    @SerializedName("score")
    @Expose public float score;


}
