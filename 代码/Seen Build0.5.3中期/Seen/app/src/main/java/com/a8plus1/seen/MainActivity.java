package com.a8plus1.seen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.a8plus1.seen.Adapter.MessageRecyclerAdapter;
import com.a8plus1.seen.Adapter.MyFragmentViewPagerAdapter;
import com.a8plus1.seen.Utils.BitmapAndStringUtils;
import com.a8plus1.seen.Utils.GoIntent;
import com.a8plus1.seen.mainViewPagers.MainFragment;
import com.a8plus1.seen.mainViewPagers.MessageFragment;
import com.a8plus1.seen.mainViewPagers.SearchFragment;
import com.a8plus1.seen.mainViewPagers.ZhenFaBuDeFragment;
import com.a8plus1.seen.mainViewPagers.ZhenZhunLeDeFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.a8plus1.seen.Bean.UserInfo;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //本地XML
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    //test Fragment ZhenFaBuDe;
    private FragmentManager manager;
    private FragmentTransaction ft;

    //我的信息
    private ImageView meImageView;
    //主页ViewPager
    private ViewPager mainViewPager;
    //底部导航栏各按钮
    private RadioButton[] radioBtns;
    private RadioGroup radioGroup;
    //侧拉菜单以及其中控件
    private SlidingMenu slidingMenu;
    private ImageView headImage;
    private Button besend;
    private Button bedone;
    private Button setting;
    private TextView nickname;
    private TextView signature;

    public static int currentPage = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this, "正在加载…", Toast.LENGTH_SHORT).show();
        initMainView();

    }

    private void initMainView() {

        //使用SharedPreferences
        pref = getSharedPreferences("Seen", MODE_PRIVATE);
        editor = pref.edit();

        //我的信息
        meImageView = findViewById(R.id.me_imageview_main);

        //主页面
        mainViewPager = findViewById(R.id.main_viewpager_main);
        initMainViewPager();

        //底部各按钮
        radioGroup = findViewById(R.id.maintab_radiogroup_main);
        radioBtns = new RadioButton[3];
        radioBtns[0] = findViewById(R.id.radio_message_main);
        radioBtns[1] = findViewById(R.id.radio_main_main);
        radioBtns[2] = findViewById(R.id.radio_search_main);
        initMainBottomButton();

        //设置侧拉菜单属性
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.left_menu);

        //侧拉菜单控件
        headImage = findViewById(R.id.image_icon);
        besend = findViewById(R.id.besend);
        bedone = findViewById(R.id.bedone);
        setting = findViewById(R.id.setting);
        nickname = findViewById(R.id.name);
        signature = findViewById(R.id.signed);


        //初始化监听
        initMainListener();

        //打开APP时尝试本地读取个人信息
        if (pref.getBoolean("loginSuccess", false)) {
            UserInfo.userID = pref.getString("userID", "");
            if (pref.getBoolean("infoOK", false)) {
                UserInfo.nickname = pref.getString("nickname", "");
                UserInfo.signature = pref.getString("signature", "");
            } else {
                UserInfo.nickname = UserInfo.userID;
                UserInfo.signature = "总想写点什么";
            }
            if (pref.getBoolean("imageExist", false)) {
                UserInfo.headImage = pref.getString("headImage", "");
                headImage.setImageBitmap(BitmapAndStringUtils.StringToBitmap(UserInfo.headImage));
            }
            nickname.setText(UserInfo.nickname);
            signature.setText(UserInfo.signature);
        }
        System.out.println("以下为本地用户信息");
        System.out.println("userID:" + pref.getString("userID", ""));
        System.out.println("nickname:" + pref.getString("nickname", ""));
        System.out.println("loginSuccess:" + pref.getBoolean("loginSuccess", false));
        System.out.println("infoOK:" + pref.getBoolean("infoOK", false));
        System.out.println("imageExist:" + pref.getBoolean("imageExist", false));
        if (!UserInfo.headImage.equals("")) {
            System.out.print("headImage:" + UserInfo.headImage.substring(UserInfo.headImage.length() - 20));
            System.out.println("头像所占空间:" + UserInfo.headImage.getBytes().length);
        }
    }

    private void initMainListener() {
        //设置侧拉菜单
        meImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingMenu.toggle(true);
            }
        });

        //侧滑菜单朕发布的控件跳转
        besend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!UserInfo.userID.equals(""))//True表示登录成功
                {
                    currentPage = 1;

                    Fragment fragment = new ZhenFaBuDeFragment();
                    manager = getSupportFragmentManager();
                    ft = manager.beginTransaction();
                    ft.replace(R.id.relativeLayout_main, fragment);
                    ft.addToBackStack("fr1");
                    ft.commit();
                    slidingMenu.toggle(false);
                } else {
                    GoIntent goIntent = new GoIntent(MainActivity.this);
                    goIntent.jump();//此处要求用户登录
                }
            }
        });


        //侧滑菜单朕准了的控件跳转
        bedone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!UserInfo.userID.equals(""))//True表示登录成功
                {
                    currentPage = 1;

                    Fragment fragment = new ZhenZhunLeDeFragment();
                    manager = getSupportFragmentManager();
                    ft = manager.beginTransaction();
                    ft.replace(R.id.relativeLayout_main, fragment);
                    ft.addToBackStack("fr2");
                    ft.commit();
                    slidingMenu.toggle(false);
                } else {
                    GoIntent goIntent = new GoIntent(MainActivity.this);
                    goIntent.jump();//此处要求用户登录
                }
            }
        });


        // 侧滑菜单设置的控件跳转
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!UserInfo.userID.equals(""))//True表示登录成功
                {
                    Intent intent = new Intent(MainActivity.this,SettingActivity.class);
                    startActivity(intent);
                } else {
                    GoIntent goIntent = new GoIntent(MainActivity.this);
                    goIntent.jump();//此处要求用户登录
                }
            }
        });


        //ViewPager
        mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //获取当前页面用于改变对应RadioButton的状态
                int current = mainViewPager.getCurrentItem() ;
                switch(current)
                {
                    case 0:
                        radioBtns[0].setChecked(true);
                        break;
                    case 1:
                        radioBtns[1].setChecked(true);
                        break;
                    case 2:
                        radioBtns[2].setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //Tab
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                //获取当前被选中的RadioButton的ID，用于改变ViewPager的当前页
                int current=0;
                switch(i)
                {
                    case R.id.radio_message_main:
                        current = 0 ;
                        break ;
                    case R.id.radio_main_main:
                        current = 1 ;
                        break;
                    case R.id.radio_search_main:
                        current = 2 ;
                        break;
                }
                if(mainViewPager.getCurrentItem() != current){
                    mainViewPager.setCurrentItem(current);
                }
            }
        });

    }

    private void initMainViewPager() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new MessageFragment());
        fragments.add(new MainFragment());
        fragments.add(new SearchFragment());

        MessageRecyclerAdapter.messageFragment = (MessageFragment) fragments.get(0);
        MessageRecyclerAdapter.mainFragment = (MainFragment) fragments.get(1);

        mainViewPager.setAdapter(new MyFragmentViewPagerAdapter(getSupportFragmentManager(), fragments));
        mainViewPager.setCurrentItem(0);
        mainViewPager.setOffscreenPageLimit(2);
    }

    private void initMainBottomButton(){
        for(int i = 0; i <  radioBtns.length; i ++ ){
            Drawable[] draid = radioBtns[i].getCompoundDrawables();

            //setBounds (1,2,3,4) 1是距左右边距离,2是距上下边距离,3是长度,4是宽度
            draid[1].setBounds(0, 0, getResources().getDimensionPixelSize(R.dimen.tab_radio_height),
                    getResources().getDimensionPixelSize(R.dimen.tab_radio_width));
            radioBtns[i].setCompoundDrawables(draid[0], draid[1], draid[2], draid[3]);
        }
    }

    //二级退出
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(currentPage == 1){
                //如果是zhenfabu 就
                currentPage = 0;
                onBackPressed();
                return false;
            }
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void exit() {
        if(slidingMenu.isMenuShowing()){
            slidingMenu.showContent();
        } else {
            if ((System.currentTimeMillis() - exitTime) > 1500) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
        }
    }
}
