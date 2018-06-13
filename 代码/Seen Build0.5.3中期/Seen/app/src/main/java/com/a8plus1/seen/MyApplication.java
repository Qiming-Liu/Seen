package com.a8plus1.seen;


import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.OkHttpNetworkExecutor;

public class MyApplication extends android.app.Application{


    @Override
    public void onCreate() {
        super.onCreate();
        //NOhttp初始化
        NoHttp.initialize(this, new NoHttp.Config()
                .setConnectTimeout(10 * 1000) // 全局连接超时时间，单位毫秒。
                .setReadTimeout(10 * 1000) // 全局服务器响应超时时间，单位毫秒。
                .setNetworkExecutor(new OkHttpNetworkExecutor())//设置底层为Okhttp
        );
    }
}
