package com.rxandroid;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.rxandroid.tools.ToastUtils;
import com.rxandroid.tools.logger.Logger;

/**
 * Created by tanzhongyang on 2016/7/6.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected String TAG;
    protected BaseApplication mApplication;
    protected Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        TAG = this.getClass().getSimpleName();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mContext = this;

        mApplication = (BaseApplication) getApplication();
    }

    /**
     * 跳转到其他页面
     * @author 谭忠扬-YuriTam
     * @2016年7月6日
     * @param tarActivity
     */
    protected void intent2Activity(Class<? extends AppCompatActivity> tarActivity) {
        Intent intent = new Intent(this, tarActivity);
        startActivity(intent);
    }

    /**
     * 提示
     * @author 谭忠扬-YuriTam
     * @2016年7月6日
     * @param msg
     */
    protected void showToast(String msg) {
        ToastUtils.showShort(this,msg);
    }

    /**
     * 打印日志
     * @author 谭忠扬-YuriTam
     * @2016年7月6日
     * @param msg
     */
    protected void showLog(String msg) {
        Logger.i(TAG,msg);
    }

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化事件
     */
    protected abstract void initEvent();
}
