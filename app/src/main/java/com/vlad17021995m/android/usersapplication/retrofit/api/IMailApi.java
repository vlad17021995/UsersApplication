package com.vlad17021995m.android.usersapplication.retrofit.api;

import com.vlad17021995m.android.usersapplication.retrofit.model.Mail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by qwerty on 04.07.2017.
 */

public interface IMailApi {
    @GET("api/check")
    Call<Mail> getMailInfo(@Query("access_key") String access_key, @Query("email") String email, @Query("format") String format);
}
