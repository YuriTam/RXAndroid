package com.test;

import android.util.Log;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by tanzhongyang on 2016/7/28.
 */
public class ObservableUtils {
    private static final String TAG = ObservableUtils.class.getSimpleName();

    public static Observable getString(){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                for (int i = 0;i < 5; i++){
                    subscriber.onNext("This is " + i + " line!");
                }
                Log.i(TAG,"Current Thread: " + Thread.currentThread().getId());
                subscriber.onCompleted();
            }

        });
    }

    public static Observable getInteger(){
        return Observable.create(new Observable.OnSubscribe<Integer>() {

            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0;i<5;i++){
                    subscriber.onNext(i);
                }
                subscriber.onCompleted();
            }
        });
    }
}
