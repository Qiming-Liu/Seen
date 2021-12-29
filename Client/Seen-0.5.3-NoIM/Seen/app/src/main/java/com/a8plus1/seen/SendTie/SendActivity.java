package com.a8plus1.seen.SendTie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.a8plus1.seen.R;
import com.yanzhenjie.nohttp.NoHttp;

public class SendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        NoHttp.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
    }
}
