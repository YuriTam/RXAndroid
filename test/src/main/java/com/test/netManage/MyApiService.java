package com.test.netManage;

import com.test.User;
import com.test.resp.HttpResult;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by tanzhongyang on 2016/7/30.
 */
public interface MyApiService {

    @GET("user/{userName}.html")
    Observable<User> getUserByName(@Path("userName") String userName);

    @POST("user/all.html")
    Observable<HttpResult<User>> getUser(@Body int userId);

    @GET("top250")
    Observable<String> getTopMovie(@Query("start") int start, @Query("count") int count);
}
