package com.a8plus1.seen.LookTie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.a8plus1.seen.R;
import com.yanzhenjie.nohttp.NoHttp;

public class LookTieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        NoHttp.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_tie);
    }
}
