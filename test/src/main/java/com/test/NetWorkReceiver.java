package com.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

public class NetWorkReceiver extends BroadcastReceiver {
    public NetWorkReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
            Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {

                }
            }).subscribe(new Action1<String>() {
                @Override
                public void call(String s) {

                }
            });
        }
    }
}
