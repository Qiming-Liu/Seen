package com.a8plus1.seen.LookTie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.a8plus1.seen.R;
import com.yanzhenjie.nohttp.NoHttp;

public class LookTieActivity extends AppCompatActivity {

    //作为全局变量
    public static String tieID;
    public static String userID;
    public static String nickNameString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        NoHttp.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_tie);
        Intent intent = this.getIntent();
        tieID = intent.getStringExtra("tieID");
        userID = intent.getStringExtra("t_userID");
        nickNameString = intent.getStringExtra("nickName");
    }
}
