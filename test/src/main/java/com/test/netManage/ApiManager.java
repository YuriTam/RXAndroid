package com.test.netManage;

import com.test.User;
import com.test.resp.HttpResult;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by tanzhongyang on 2016/7/30.
 */
public class ApiManager {
    public static final String BASE_URL = "https://127.0.0.1:8080/Server/";
    private static ApiManager apiManage;
    private MyApiService myApiService;
    private Retrofit retrofit;

    public ApiManager(){
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(60, TimeUnit.MILLISECONDS);   //设置连接超时

        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        myApiService = retrofit.create(MyApiService.class);
    }

    public static ApiManager getInstance(){
        if(apiManage == null){
            synchronized (ApiManager.class){
                if(apiManage == null){
                    apiManage = new ApiManager();
                }
            }
        }
        return apiManage;
    }

    public Observable<User> getUserByName(String userName){
        return myApiService.getUserByName(userName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<String> getTopMovie(int start,int count){
        return myApiService.getTopMovie(start,count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<HttpResult<User>> getUser(int userId){
        return myApiService.getUser(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
