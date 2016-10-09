package com.test;

import rx.Subscription;

/**
 * Created by tanzhongyang on 2016/7/29.
 */
public class RxUtils {

    /**
     * 取消订阅
     * @param subscription
     */
    public static void unsubscribeIfNotNull(Subscription subscription) {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }
}
