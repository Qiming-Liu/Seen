package com.a8plus1.seen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.a8plus1.seen.Bean.NetData;
import com.a8plus1.seen.Bean.UserInfo;
import com.a8plus1.seen.Utils.BitmapAndStringUtils;
import com.a8plus1.seen.Utils.HandleImage;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.StringRequest;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import io.rong.imkit.RongIM;


public class SettingActivity extends AppCompatActivity {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    EditText ETnickname;
    EditText ETsignature;
    ImageView back;//返回按钮
    ImageView enter;//返回按钮

    //计算时长
    Long time;
    Date date;

    //修改头像用
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    private ImageView iv_personal_icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //使用SharedPreferences
        pref = getSharedPreferences("Seen", MODE_PRIVATE);
        editor = pref.edit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //获取view
        ETnickname = findViewById(R.id.editText1);
        ETsignature = findViewById(R.id.editText2);
        back = findViewById(R.id.back);
        enter = findViewById(R.id.enter);
        iv_personal_icon = findViewById(R.id.ImageView0);

        System.out.println("[ProSS]读取UserInfo并显示在界面上");
        ETnickname.setText(UserInfo.nickname);
        ETsignature.setText(UserInfo.signature);
        iv_personal_icon.setImageBitmap(BitmapAndStringUtils.StringToBitmap(UserInfo.headImage));
    }

    public void back(View view) {
        //跳转
        Intent intent =new Intent(SettingActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void exit(View view) {
        System.out.println("[ProSS]退出登录清除信息并重启");
        editor.putBoolean("loginSuccess", false);
        editor.putBoolean("tokenOK", false);
        editor.putBoolean("imageExist", false);
        editor.putString("headImage", "");
        editor.putString("token", "");
        UserInfo.clean();
        editor.commit();
        RongIM.getInstance().logout();
        final Intent intent = this.getPackageManager().getLaunchIntentForPackage(this.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void enter(View view) {//个人信息提交

        //提交信息到服务器
        Request<String> request = new StringRequest(NetData.urlInfo, RequestMethod.POST);//传递一个get请求
        if (UserInfo.userID.equals("") == false) {
            JSONObject jsonObject = new JSONObject();
            try {
                //在这里面写请求内容，转化为json格式
                jsonObject.put("userID", UserInfo.userID);
                jsonObject.put("nickname", ETnickname.getText().toString());
                jsonObject.put("signature", ETsignature.getText().toString());
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
                        Toast.makeText(getApplicationContext(), "更新个人资料成功", Toast.LENGTH_SHORT).show();
                        //缓存保存信息
                        try {
                            UserInfo.nickname = ETnickname.getText().toString();
                            UserInfo.signature = ETsignature.getText().toString();
                            System.out.println("[ProSS]缓存保存个人信息");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //本地保存信息
                        editor.putString("nickname", UserInfo.nickname);
                        editor.putString("signature", UserInfo.signature);
                        editor.putBoolean("loginSuccess", true);
                        editor.commit();
                        System.out.println("[ProSS]本地保存个人信息");

                        //跳转
                        Intent intent =new Intent(SettingActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        System.out.println("[ProSS]更新个人资料失败");
                    }
                }

                @Override
                public void onFailed(int what, Response<String> response) {
                    System.out.println("[ProSS]更新信息超时");
                }

                @Override
                public void onStart(int what) {
                }

                @Override
                public void onFinish(int what) {
                    finish();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "无法获取用户ID", Toast.LENGTH_SHORT).show();
        }
    }

    public void showChoosePicDialog(View view) { //点击事件
        System.out.println("[ProSS]本地选择照片");

        Intent openAlbumIntent = new Intent(
                Intent.ACTION_GET_CONTENT);
        openAlbumIntent.setType("image/*");
        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    public void startPhotoZoom(Uri uri) {//裁剪图片方法实现
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 64);
        intent.putExtra("outputY", 64);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    public void setImageToView(Intent data) {//保存裁剪之后的图片数据
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            photo = HandleImage.toRoundBitmap(photo, tempUri); // 这个时候的图片已经被处理成圆形的了
            iv_personal_icon.setImageBitmap(photo);
            uploadPic(photo);
        }
    }

    public void uploadPic(Bitmap bitmap) {
        // 把Bitmap转换成String
        // 注意这里得到的图片已经是圆形图片了

        //缓存保存
        UserInfo.headImage = BitmapAndStringUtils.BitmapToString(bitmap);

        //本地保存
        editor.putBoolean("imageExist", true);
        editor.putString("headImage", UserInfo.headImage);
        editor.commit();

        //网络上传
        Request<String> request = new StringRequest(NetData.urlIcon, RequestMethod.POST);
        JSONObject jsonObject = new JSONObject();
        try {
            //在这里面写请求内容，转化为json格式
            jsonObject.put("userID", UserInfo.userID);
            jsonObject.put("headImage", UserInfo.headImage);
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
                    System.out.println("[ProSS]头像上传成功");
                    Toast.makeText(getApplicationContext(), "头像修改成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "头像修改失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                Toast.makeText(getApplicationContext(), "连接服务器超时！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStart(int what) {
                System.out.println("[ProSS]正在上传头像");
                System.out.print("[ProSS]headImage:" + UserInfo.headImage.substring(UserInfo.headImage.length() - 20));
                System.out.println("       所占空间:" + UserInfo.headImage.getBytes().length);

                date = new Date();
                time = date.getTime();

            }

            @Override
            public void onFinish(int what) {
                date = new Date();
                time = date.getTime() - time;
                System.out.println("[ProSS]用时" + time + "毫秒");
            }
        });
    }
}
