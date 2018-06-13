package com.a8plus1.seen;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.a8plus1.seen.Bean.NetData;
import com.a8plus1.seen.Bean.UserInfo;
import com.a8plus1.seen.Json.JUser;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.OkHttpNetworkExecutor;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @InjectView(R.id.et_username)
    EditText etUsername;
    @InjectView(R.id.et_password)
    EditText etPassword;
    @InjectView(R.id.bt_login)
    Button btGo;
    @InjectView(R.id.cv)
    CardView cv;
    @InjectView(R.id.fab)
    FloatingActionButton fab;

    String userID = "";
    String password = "";
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //使用SharedPreferences
        pref = getSharedPreferences("Seen", MODE_PRIVATE);
        editor = pref.edit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

    }

    @OnClick({R.id.bt_login, R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(this, fab, fab.getTransitionName());
                    startActivity(new Intent(this, SignupActivity.class), options.toBundle());
                } else {
                    startActivity(new Intent(this, SignupActivity.class));
                }
                break;
            case R.id.bt_login:
                System.out.println("[ProSS]登录按钮点击");
                Explode explode = new Explode();
                explode.setDuration(500);

                getWindow().setExitTransition(explode);
                getWindow().setEnterTransition(explode);

                userID = etUsername.getText().toString();
                password = etPassword.getText().toString();

                if (!(userID.equals("") || password.equals(""))) {//判空

                    Request<String> request = new StringRequest(NetData.urlLogin, RequestMethod.POST);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        //在这里面写请求内容，转化为json格式
                        jsonObject.put("userID", userID);
                        jsonObject.put("password", password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    request.setDefineRequestBodyForJson(jsonObject);

                    //开始通信
                    RequestQueue queue = NoHttp.newRequestQueue();//创建一个请求队列
                    queue.add(0, request, new OnResponseListener<String>() {
                        @Override
                        public void onSucceed(int what, Response<String> response) {
                            if (response.responseCode() == 200) {
                                //网络请求用户信息保存在本地
                                if (JUser.getSuccess(response.get().toString())) {
                                    Toast.makeText(getApplicationContext(), userID + "登录成功", Toast.LENGTH_SHORT).show();

                                    System.out.println("[ProSS]获取" + userID + "的个人信息");
                                    Request<String> request = new StringRequest(NetData.urlGetInfo, RequestMethod.POST);
                                    JSONObject jsonObject = new JSONObject();
                                    try {
                                        //在这里面写请求内容，转化为json格式
                                        jsonObject.put("userID", userID);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    request.setDefineRequestBodyForJson(jsonObject);

                                    //开始通信
                                    RequestQueue queue = NoHttp.newRequestQueue();//创建一个请求队列
                                    queue.add(0, request, new OnResponseListener<String>() {
                                        @Override
                                        public void onSucceed(int what, Response<String> response) {
                                            if (response.responseCode() == 200) {

                                                System.out.println("[ProSS]用户信息存入UserInfo");
                                                JUser.getUserInfo(response.get().toString());

                                                editor.putString("userID", UserInfo.userID);
                                                editor.putString("nickname", UserInfo.nickname);
                                                editor.putString("signature", UserInfo.signature);
                                                editor.putString("token", UserInfo.token);

                                                if (!UserInfo.userID.equals(""))
                                                    editor.putBoolean("loginSuccess", true);

                                                if (!UserInfo.token.equals(""))
                                                    editor.putBoolean("tokenOK", true);

                                                //开始获取头像
                                                System.out.println("[ProSS]获取头像");
                                                Request<String> request = new StringRequest(NetData.urlGetIcon, RequestMethod.POST);
                                                JSONObject jsonObject = new JSONObject();
                                                try {
                                                    //在这里面写请求内容，转化为json格式
                                                    jsonObject.put("userID", userID);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                request.setDefineRequestBodyForJson(jsonObject);

                                                //开始通信
                                                RequestQueue queue = NoHttp.newRequestQueue();//创建一个请求队列
                                                queue.add(0, request, new OnResponseListener<String>() {
                                                    @Override
                                                    public void onSucceed(int what, Response<String> response) {
                                                        if (response.responseCode() == 200) {
                                                            try {
                                                                JSONObject jo = new JSONObject(response.get().toString());
                                                                UserInfo.headImage = jo.getString("headImage");
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                            System.out.println("[ProSS]头像获取成功");
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailed(int what, Response<String> response) {
                                                        Toast.makeText(getApplicationContext(), "获取头像失败", Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void onStart(int what) {
                                                    }

                                                    @Override
                                                    public void onFinish(int what) {
                                                    }
                                                });
                                                //头像获取结束
                                                if (!UserInfo.headImage.equals(""))
                                                    editor.putBoolean("imageExist", true);

                                                editor.commit();
                                                System.out.println("以下为登陆用户信息");
                                                System.out.println("userID:" + pref.getString("userID", ""));
                                                System.out.println("nickname:" + pref.getString("nickname", ""));
                                                System.out.println("loginSuccess:" + pref.getBoolean("loginSuccess", false));
                                                System.out.println("tokenOK:" + pref.getBoolean("tokenOK", false));
                                                System.out.println("imageExist:" + pref.getBoolean("imageExist", false));
                                                System.out.println("token:" + pref.getString("token", ""));
                                                if (!UserInfo.headImage.equals("")) {
                                                    System.out.print("headImage:" + UserInfo.headImage.substring(UserInfo.headImage.length() - 20));
                                                    System.out.println("头像所占空间:" + UserInfo.headImage.getBytes().length);
                                                }

                                                //跳回主界面
                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();

                                            } else {
                                                System.out.println("[ProSS]服务器响应失败");
                                            }
                                        }

                                        @Override
                                        public void onFailed(int what, Response<String> response) {
                                            System.out.println("[ProSS]获取信息超时！");
                                        }

                                        @Override
                                        public void onStart(int what) {

                                        }

                                        @Override
                                        public void onFinish(int what) {
                                        }
                                    });

                                } else {//登录失败
                                    System.out.println("[ProSS]登录失败清除信息");
                                    editor.putBoolean("loginSuccess", false);
                                    editor.putBoolean("tokenOK", false);
                                    editor.putBoolean("imageExist", false);
                                    editor.putString("headImage", "");
                                    editor.putString("token", "");
                                    UserInfo.clean();
                                    editor.commit();
                                    Toast.makeText(getApplicationContext(), "用户名或密码不正确", Toast.LENGTH_SHORT).show();
                                }
                            }


                        }

                        @Override
                        public void onFailed(int what, Response<String> response) {
                            System.out.println("[ProSS]登录超时");
                            editor.putBoolean("loginSuccess", false);
                            editor.putBoolean("tokenOK", false);
                            editor.putBoolean("imageExist", false);
                            editor.putString("headImage", "");
                            editor.putString("token", "");
                            UserInfo.clean();
                            editor.commit();
                            Toast.makeText(getApplicationContext(), "连接服务器超时！请重新登录", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onStart(int what) {
                            System.out.println("[ProSS]正在登录服务器");
                        }

                        @Override
                        public void onFinish(int what) {
                            System.out.println("[ProSS]登录通信完成");
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
