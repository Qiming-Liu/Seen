package com.a8plus1.seen.LookTie;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.a8plus1.seen.Bean.NetData;
import com.a8plus1.seen.Bean.UserInfo;
import com.a8plus1.seen.R;
import com.a8plus1.seen.SendTie.BitmapAndStringUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LookTieFragment extends Fragment {

    private ImageView down,headImage,reply_headImage,sendreply;
    private TextView send_looknumber,nickname,time,tital;
    private EditText sendText;
    private ReplyAdapter adapter;
    private RecyclerView recyclerView;
    private int falg=0,tmep_agree=0;
    private BitmapAndStringUtils b;
    private ArrayList<Tiezi> tiezis;
    private Bitmap bp;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_looktie, container, false);
        initLookTieFragment(view);

        return view;
    }

    private void initLookTieFragment(View view) {
        down=(ImageView)view.findViewById(R.id.down);//向下按钮 返回
        headImage=(ImageView)view.findViewById((R.id.headImage));//人物头像
        reply_headImage=(ImageView)view.findViewById(R.id.reply_headImage);//回复人物头像
        sendreply=(ImageView)view.findViewById(R.id.sendreply);//发送回复
        send_looknumber=(TextView)view.findViewById(R.id.send_looknumber);//已阅量
        nickname=(TextView)view.findViewById(R.id.nickname);//昵称
        time=(TextView)view.findViewById(R.id.time);//时间
        tital=(TextView)view.findViewById(R.id.tital);//标题
        sendText=(EditText)view.findViewById(R.id.sendText);//回复内容
         b = new BitmapAndStringUtils();//Bitmap String 转换
        tiezis=new ArrayList<>();
        initListener();
        recyclerView = (RecyclerView)view.findViewById(R.id.lv);
        LinearLayoutManager layoutManager =new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ReplyAdapter(this.getContext());
        recyclerView.setAdapter(adapter);
        initTiezi();
        adapter.setClickListener(new ReplyAdapter.OnItemClickListener(){

            @Override
            public void onClick(View view, int position) {
                //Toast.makeText(getContext(),"跳转显示个人信息", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void initListener() {
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //返回按钮
                getActivity().finish();
                //Toast.makeText(getContext(), "点击返回按钮", Toast.LENGTH_SHORT).show();
            }
        });
        headImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ////跳转显示个人信息

                //Toast.makeText(getContext(), "跳转显示个人信息", Toast.LENGTH_SHORT).show();
            }
        });

//        send_good_picture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //点赞图片
//                changeGood(falg);
//                Toast.makeText(getContext(), "点赞图片", Toast.LENGTH_SHORT).show();
//            }
//        });
        sendreply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //发送回复
                sendResultWithNohttp_send();
                Toast.makeText(getContext(), "发送回复", Toast.LENGTH_SHORT).show();
            }
        });
//        send_good.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //点赞信息
//                changeGood(falg);
//                Toast.makeText(getContext(), "点赞信息", Toast.LENGTH_SHORT).show();
//
//            }
//        });
    }

    private void sendResultWithNohttp_look() {
        //发送请求 读取帖子相关信息和评论 tieID 返回200 content userID time titile circleImage nickname agree pageviews    urlGetNote  urlGetReply
        if(tiezis.size() != 0) tiezis.removeAll(tiezis);
        Intent intent = getActivity().getIntent();
        String tieID = intent.getStringExtra("tieID");
        Request<String> request = NoHttp.createStringRequest(NetData.urlGetNote, RequestMethod.POST);
        RequestQueue queue = NoHttp.newRequestQueue();
        JSONObject jsonObject = new JSONObject();
        try {
            //在这里面写请求内容，转化为json格式
            jsonObject.put("tieID", tieID);//差帖子ID
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setDefineRequestBodyForJson(jsonObject);
        queue.add(1, request, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {
                System.out.println("正在登录服务器--请求帖子数据");
            }
            @Override
            public void onSucceed(int what, Response<String> response) {//content  userID time titile Image1 Image2 Image3 circleImage nickname agree pageviews
                if(response.responseCode() == 200){
                    System.out.println("登录通信服务器成功");
                    String result = response.get();
                    String userId="";//获取人物头像
                    try {
                        //开始解析
                        JSONObject jsonObjectTemp = null;
                        JSONArray jsonArray = new JSONArray(result);
                        jsonObjectTemp = (JSONObject) jsonArray.get(0);
                        Intent intent = getActivity().getIntent();
                        String nickname123 = jsonObjectTemp.optString("nickname","");
                        String userID = jsonObjectTemp.optString("t_userID","已阅");
                        if (nickname123.equals("")) nickname.setText(userID);
                        else nickname.setText(jsonObjectTemp.optString("nickname", nickname123));
                        System.out.println("1");
                        tital.setText(jsonObjectTemp.getString("title"));
                        System.out.println("2");
                        String time123 = jsonObjectTemp.getString("time");
                        time.setText(time123.substring(5, 16));
                        System.out.println("3");
                        tmep_agree=jsonObjectTemp.getInt("agree");
                        System.out.println("4");
                        send_looknumber.setText(Integer.toString(jsonObjectTemp.getInt("pageviews")));
                        String s,s1,s2,s3;
                        if(jsonObjectTemp.optString("circleImage","").equals("")){
                            Resources resources = getContext().getResources();
                            Drawable drawable = resources.getDrawable(R.drawable.de_head);
                            headImage.setBackground(drawable);
                        }else {
                            s = jsonObjectTemp.getString("circleImage");
                            bp=b.convertStringToIcon(s);
                            headImage.setImageBitmap(bp);
                        }
                        if(jsonObjectTemp.optString("Image1","").equals("")){
                            s1=" ";
                        }else {
                            s1=jsonObjectTemp.getString("Image1");
                        }
                        if(jsonObjectTemp.optString("Image1","").equals("")){
                            s2=" ";
                        }else {
                            s2=jsonObjectTemp.getString("Image2");
                        }
                        if(jsonObjectTemp.optString("Image1","").equals("")){
                            s3=" ";
                        }else {
                            s3=jsonObjectTemp.getString("Image3");
                        }

                         Tiezi a1 = new Tiezi(jsonObjectTemp.getString("content"),b.convertStringToIcon(s1),b.convertStringToIcon(s2),b.convertStringToIcon(s3),1);
                        tiezis.add(a1);
                        adapter.addList(tiezis);
                        adapter.notifyDataSetChanged();
                        if(!tiezis.isEmpty()) tiezis.removeAll(tiezis);
                        System.out.println("完成第一部分解析");
                        for(int i = 1; i < jsonArray.length(); i ++){
                            System.out.println("----------------------------------");
                            //开始第二部分发送 回复人物
                            JSONObject jsonObject_userID = (JSONObject) jsonArray.get(i);
                            final String replyID =jsonObject_userID.getString("c_userID");
                            final String replyTime =jsonObject_userID.getString("c_time");
                            final String reply_content =jsonObject_userID.getString("content");
                            Request<String> request = NoHttp.createStringRequest(NetData.urlGetInfo, RequestMethod.POST);
                            RequestQueue queue_reply = NoHttp.newRequestQueue();
                            JSONObject jsonObjecttemp = new JSONObject();
                            try {
                                //在这里面写请求内容，转化为json格式
                                jsonObjecttemp.put("userID",replyID);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            request.setDefineRequestBodyForJson(jsonObjecttemp);
                            queue_reply.add(1, request, new OnResponseListener<String>(){

                                @Override
                                public void onStart(int what) {
                                    System.out.println("开始第二部分通信服务器");
                                }

                                @Override
                                public void onSucceed(int what, Response<String> response) {
                                    if(response.responseCode() == 200){
                                        System.out.println("登录第二部分通信服务器成功");
                                        String result_temp = response.get();
                                        try {
                                           JSONObject jsonObject_temp=new JSONObject(result_temp);
                                            String reply_nickname,reply_headImage;
                                            if(jsonObject_temp.optString("nickname","").equals("")){
                                                reply_nickname=replyID;
                                            }else {
                                                reply_nickname=jsonObject_temp.getString("nickname");
                                            }
                                            if(jsonObject_temp.optString("nickname","").equals("")){
                                               reply_headImage="  ";
                                            }else {
                                                reply_headImage = jsonObject_temp.getString("headImage");
                                            }
                                            Bitmap reply_bp=b.convertStringToIcon(reply_headImage);
                                            Tiezi a2 = new Tiezi(reply_content,reply_nickname,replyTime,reply_bp,2);
                                            tiezis.add(a2);
                                            adapter.addList(tiezis);
                                            adapter.notifyDataSetChanged();
                                            System.out.println("--------"+tiezis.size());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            System.out.println("未完成解析");
                                        }
                                    }

                                }

                                @Override
                                public void onFailed(int what, Response<String> response) {
                                    System.out.println("第二部分通信服务器失败");
                                }

                                @Override
                                public void onFinish(int what) {
                                    System.out.println("第二部分通信服务器完成");
                                }
                            });

                        }
                        System.out.println("完成解析");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        System.out.println("未完成解析");
                    }
                    adapter.addList(tiezis);
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });
        System.out.println("--------"+tiezis.size());
    }
    private void sendResultWithNohttp_look_reply(){
        //发送请求 读取帖子评论 tieID 返回200 content userID time titile circleImage nickname agree pageviews

    }

    private void sendResultWithNohttp_send() {//发送请求 发送评论 tieID uerID content time 返回200
        String content = sendText.getText().toString();
        SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyy-MM-dd hh:mm:ss");//主界面显示了年月日 所以不显示年月日了
        Date curDate =  new Date(System.currentTimeMillis());
        String localtime=formatter.format(curDate);//获取本地时间
        Request<String> request =  new StringRequest(NetData.urlReply, RequestMethod.POST);
        JSONObject jsonObject = new JSONObject();
        try {
            //在这里面写请求内容，转化为json格式
            Intent intent = getActivity().getIntent();
            String tieID = intent.getStringExtra("tieID");
            jsonObject.put("tieID", tieID);// 差帖子ID
            jsonObject.put("c_userID", UserInfo.userID);
            jsonObject.put("content",content);
            jsonObject.put("c_time",localtime);;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setDefineRequestBodyForJson(jsonObject);
        RequestQueue queue = NoHttp.newRequestQueue();
        queue.add(0, request, new OnResponseListener<String>(){

            @Override
            public void onStart(int what) {
                System.out.println("发送回复");
            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                if(response.responseCode() == 200){
                    Toast.makeText(getContext(), "正在加载…", Toast.LENGTH_SHORT).show();
                    Intent intent = getActivity().getIntent();
                    getActivity().finish();
                    startActivity(intent);
                }else{
                    System.out.println("发送回复失败");
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                System.out.println("发送回复失败");
            }
            @Override
            public void onFinish(int what) {
                System.out.println("发送回复失败");
            }
        });


    }
    private void initTiezi() {
        sendResultWithNohttp_look();//读取帖子相关信息和评论
        System.out.println("--------"+tiezis.size());

    }
}