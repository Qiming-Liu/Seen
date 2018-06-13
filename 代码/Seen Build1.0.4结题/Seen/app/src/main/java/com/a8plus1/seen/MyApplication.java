package com.a8plus1.seen;


import android.net.Uri;

import com.a8plus1.seen.Bean.MIFriend;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.OkHttpNetworkExecutor;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

public class MyApplication extends android.app.Application implements RongIM.UserInfoProvider{

    public static List<MIFriend> friends = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        //NOhttp初始化
        NoHttp.initialize(this, new NoHttp.Config()
                .setConnectTimeout(10 * 1000) // 全局连接超时时间，单位毫秒。
                .setReadTimeout(10 * 1000) // 全局服务器响应超时时间，单位毫秒。
                .setNetworkExecutor(new OkHttpNetworkExecutor())//设置底层为Okhttp
        );

        //融云初始化
        RongIM.init(this);
        //动态获取他人头像信息
        RongIM.setUserInfoProvider(this, true);
    }

    public UserInfo getUserInfo(String s) {
        for (MIFriend i : friends) {
            if (i.getUserId().equals(s)) {
                return new UserInfo(i.getUserId(), i.getName(), Uri.parse("https://free.modao.cc/uploads3/icons/623/6235610/retina_icon_1513177390.png"));
            }
        }
        return null;
    }
}
