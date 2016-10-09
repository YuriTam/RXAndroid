package com.test;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.test.netManage.ApiManager;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends RxAppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Integer[] args = {2,6,41,22,6,1,69,5,22,5,2,12};

    @BindView(R.id.tv_test)
    TextView tvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Log.i(TAG,"onCreate Thread:" + Thread.currentThread().getId());
    }

    private Observable getDataString() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                for (int i = 0; i < 5; i++) {
                    subscriber.onNext("line:" + i);
                }
                subscriber.onCompleted();
            }
        });
    }

    private void outPrintln_1() {
        ObservableUtils.getString()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG,e.getMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        Log.i(TAG, "onNext: " + s + " onNext Thread:" + Thread.currentThread().getId());
                        tvTest.append(s);
                    }
                });
    }

    private void outPrintln_2() {
        ObservableUtils.getInteger()
                .map(new Func1<Integer,Integer>() {
                    @Override
                    public Integer call(Integer s) {
                        return s + 1;
                    }
                })
                .map(new Func1<Integer,String>() {
                    @Override
                    public String call(Integer integer) {
                        return String.valueOf(integer * 10);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted");
                        String result = tvTest.getText().toString();
                        tvTest.setText(result.substring(0,result.length()-4));
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        Log.i(TAG,"onStart");
                    }

                    @Override
                    public void onError(Throwable e) {
                        tvTest.append(e.getMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        Log.i(TAG, "The integer is : " + s + " onNext Thread:" + Thread.currentThread().getId());
                        tvTest.append(s + " -> ");
                    }
                });
    }

    private void testArrays() {
        Observable.from(args)
                .skip(5)
                .take(5000, TimeUnit.MILLISECONDS, Schedulers.newThread())
                /*.flatMap(new Func1<Integer, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(Integer integer) {
                        return Observable.just(integer);
                    }
                })*/
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer > 100;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.i(TAG,"call:" + integer);
                    }
                });
    }

    private void showToast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.btn_string, R.id.btn_integer, R.id.btn_arrays})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_string:
                //outPrintln_1();
                Observable.merge(Observable.just(3),Observable.just(45),Observable.just("df"))
                        .subscribeOn(Schedulers.io())
                        .flatMap(new Func1<Serializable, Observable<String>>() {
                            @Override
                            public Observable<String> call(Serializable serializable) {
                                return Observable.just(serializable.toString());
                            }
                        })
                        .filter(new Func1<String, Boolean>() {
                            @Override
                            public Boolean call(String s) {
                                return s.equals("45");
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                Log.i(TAG,"并发处理数据：" + s);
                                tvTest.append(s);
                            }
                        });
                break;
            case R.id.btn_integer:
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
                break;
            case R.id.btn_arrays:
                //testArrays();
                ApiManager.getInstance().getTopMovie(0,10)
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {
                                Log.i(TAG,"onCompleted");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.i(TAG,"onError：" + e.getMessage());
                            }

                            @Override
                            public void onNext(String s) {
                                Log.i(TAG,"onNext：" + s);
                            }
                        });
                /*Observable.from(args)
                        .distinct()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                Log.i(TAG,"去重后的结果：" + integer);
                            }
                        });*/
                /*Observable.timer(2,TimeUnit.SECONDS)
                        .subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                Log.i(TAG,"2秒执行：" + aLong);
                            }
                        });*/

                /*Observable.concat(Observable.just(2),Observable.just(3),Observable.just(9))
                        .first()
                        .compose(this.<Integer>bindToLifecycle())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                Log.i(TAG,"concat：" + integer);
                            }
                        });*/
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.finish();
    }
}
