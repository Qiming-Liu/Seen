package com.a8plus1.seen.Utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.content.Intent;

import com.a8plus1.seen.LoginActivity;
import com.a8plus1.seen.MainActivity;

public class GoIntent {
    private Context mainActivity ;
    public GoIntent(Context mainActivity){
        this.mainActivity = mainActivity;
    }
    public void jump(){

        System.out.println("[ProSS]要求用户登录");

        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);//1.建立builder对象
        builder.setTitle("您还没有登录");//2.设置标题区
//        builder.setIcon(R.mipmap.ic_launcher);//3.设置图标区
        builder.setMessage("登录后才能继续");//4.设置内容区
        //5.设置按钮区
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton("前往登录", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(mainActivity, LoginActivity.class);
                mainActivity.startActivity(intent);
            }
        });
        builder.setCancelable(false);//设置对话框只能点击按钮取消
        AlertDialog dialog = builder.create();//6.创建对话框
        dialog.show();//7.让对话框显示出来
    }
}
