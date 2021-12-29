package com.a8plus1.seen;

import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.URLConnectionNetworkExecutor;

public class MyApplication extends android.app.Application{


    @Override
    public void onCreate() {
        super.onCreate();
        Logger.setDebug(true); // 开启NoHttp调试模式。
        Logger.setTag("HTTP"); // 设置NoHttp打印Log的TAG。

        //NOhttp初始化 1.1.1
        NoHttp.initialize(this, new NoHttp.Config()
                .setConnectTimeout(10 * 1000) // 全局连接超时时间，单位毫秒。
                .setReadTimeout(10 * 1000) // 全局服务器响应超时时间，单位毫秒。
                .setNetworkExecutor(new URLConnectionNetworkExecutor())//设置底层为Okhttp
        );

//        //NOhttp初始化 1.1.11
//        InitializationConfig config = InitializationConfig.newBuilder(this)
//                // 全局连接服务器超时时间，单位毫秒，默认10s。
//                .connectionTimeout(30 * 1000)
//                // 全局等待服务器响应超时时间，单位毫秒，默认10s。
//                .readTimeout(30 * 1000)
//                .build();
//
//        NoHttp.initialize(config);
    }
}
