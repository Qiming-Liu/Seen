package com.a8plus1.seen.mainViewPagers.MI;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.a8plus1.seen.MainActivity;
import com.a8plus1.seen.R;


public class ConversationActivity extends FragmentActivity {

    private ImageView backImageView;
    private TextView titleTextView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        initView();
    }

    private void initView() {

        backImageView = findViewById(R.id.back_imageview_conv);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                MainActivity.mainViewPager.setCurrentItem(3);
                finish();
            }
        });

        titleTextView = findViewById(R.id.title_textview_conv);
        String title = getIntent().getData().getQueryParameter("title");
        if(TextUtils.isEmpty(title)){
            titleTextView.setText("聊天窗口");
        }
    }
}
