package com.test.respListener;

/**
 * Created by tanzhongyang on 2016/8/3.
 */
public interface SubscriberOnNextListener<T> {
    void onNext(T t);
}
