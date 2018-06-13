package com.a8plus1.seen.Json;

import com.a8plus1.seen.Bean.UserInfo;

import org.json.JSONObject;
import org.json.JSONException;



public class JUser {

    public static Boolean getSuccess(String data) {//获取登录注册信息
        Boolean code = false;
        try {

            JSONObject jsonObject = new JSONObject(data);
            code = jsonObject.getBoolean("code");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return code;
    }

    public static void getUserInfo(String data) {//获取用户信息
        try {
            JSONObject jo = new JSONObject(data);
            UserInfo.userID = jo.getString("userID");
            UserInfo.nickname = jo.getString("nickname");
            UserInfo.signature = jo.getString("signature");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
