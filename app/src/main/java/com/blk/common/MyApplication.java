package com.blk.common;

import android.app.Application;
import android.content.Context;

/**
 * Created by wangwentao on 2017/2/21.
 */

public class MyApplication extends Application {
    private static Context context;//全局上下文

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    //获取全局的上下文
    public static Context getContext(){
        return context;
    }
}

