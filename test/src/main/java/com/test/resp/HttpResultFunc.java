package com.test.resp;

import rx.functions.Func1;

/**
 * Created by tanzhongyang on 2016/7/30.
 */
public class HttpResultFunc<T> implements Func1<HttpResult<T>,T>{

    @Override
    public T call(HttpResult<T> httpResult) {
        if(httpResult.getResultCode() != 200){
            new ApiException(httpResult.getResultCode());
        }
        return httpResult.getData();
    }
}
