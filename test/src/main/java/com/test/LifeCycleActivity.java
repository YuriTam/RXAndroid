package com.test;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class LifeCycleActivity extends RxAppCompatActivity {
    private static final String TAG = LifeCycleActivity.class.getSimpleName();

    @BindView(R.id.tv_result)
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_cycle);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_test)
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_test:
                getTimes();
                lunXun();
                break;
        }
    }

    private void getTimes(){
        Observable.interval(0,1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                //.compose(this.<Long>bindToLifecycle())
                .compose(this.<Long>bindUntilEvent(ActivityEvent.STOP))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Log.i(TAG,"interval: " + aLong);
                        tvResult.setText("测试结果：" + aLong);
                    }
                });
    }

    private void lunXun(){
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                Schedulers.newThread().createWorker()
                        .schedulePeriodically(new Action0() {
                            @Override
                            public void call() {
                                subscriber.onNext("123456");
                            }
                        },1,2,TimeUnit.SECONDS);
            }
        })
        .compose(this.<String>bindToLifecycle())
        .subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i(TAG,"轮询执行：" + s);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"LifeCycleActivity is finish.");
        this.finish();
    }
}
