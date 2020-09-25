package com.a8plus1.seen.Bean;

import android.content.Context;

import android.net.Uri;
import android.widget.Toast;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;


public class IMData {
    private Context mainActivity;
    String time = "";
    String ak = "x4vkb1qpxfwxk";
    String Nonce = "";
    String Signature = "";

    public IMData(Context mainActivity) {
        this.mainActivity = mainActivity;
    }//构造函数传入activity

    public static String shaEncrypt(String strSrc) {//哈希算法
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = strSrc.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-1");// 将此换成SHA-1、SHA-512、SHA-384等参数
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }
    public void getIn(){
        long t = System.currentTimeMillis() / 1000;//获取系统时间的10位的时间戳
        time = String.valueOf(t);
        Random rand = new Random();
        Nonce = Integer.toString(rand.nextInt(10000));
        Signature = "App Secret" + Nonce + time;
    }

    public void getToken() {
        Uri uri = Uri.parse("https://free.modao.cc/uploads3/icons/623/6235610/retina_icon_1513177390.png");
        String urlIM = "http://api.cn.ronghub.com/user/getToken.json";
        Request<String> request = new StringRequest(urlIM, RequestMethod.POST);

        request.addHeader("App-Key", ak);
        request.addHeader("Nonce", Nonce);
        request.addHeader("Timestamp", time);
        request.addHeader("Signature", shaEncrypt(Signature));
        //http请求头
        request.add("userId", UserInfo.userID);
        request.add("name", UserInfo.nickname);
        request.add("portraitUri", uri.toString());
        //http请求内容

        RequestQueue queue = NoHttp.newRequestQueue();
        queue.add(0, request, new OnResponseListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                if (response.responseCode() == 200) {
                    try {
                        JSONObject jo = new JSONObject(response.get().toString());
                        String str = jo.getString("token");
                        //str就是token

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(mainActivity.getApplicationContext(), Integer.toString(response.responseCode()), Toast.LENGTH_SHORT).show();//报错信息
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
            }

            @Override
            public void onStart(int what) {
            }

            @Override
            public void onFinish(int what) {
            }
        });
    }
}