package com.a8plus1.seen.Adapter;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.a8plus1.seen.Bean.NetData;
import com.a8plus1.seen.Bean.UserInfo;
import com.a8plus1.seen.LookTie.LookTieActivity;
import com.a8plus1.seen.R;
import com.a8plus1.seen.TieZi;
import com.a8plus1.seen.Utils.BitmapAndStringUtils;
import com.a8plus1.seen.Utils.GoIntent;
import com.a8plus1.seen.mainViewPagers.MainFragment;
import com.a8plus1.seen.mainViewPagers.MessageFragment;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MessageRecyclerAdapter extends RecyclerView.Adapter {

    private ArrayList<TieZi> tieZis = new ArrayList<>();

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private Activity mainActivity;

    //存储已阅帖子
    public static  ArrayList<TieZi> watchTieZis = new ArrayList<>();
    //更新UI
    public static MessageFragment messageFragment;
    public static MainFragment mainFragment;

    public MessageRecyclerAdapter(ArrayList<TieZi> tieZis){
        this.tieZis = tieZis;
    }

    public MessageRecyclerAdapter(ArrayList<TieZi> tieZis, Activity mainActivity) {
        this.tieZis = tieZis;
        this.mainActivity = mainActivity;
    }

    public static void reFreshUI(){
        if(messageFragment != null || mainFragment != null){
            messageFragment.refreshUI();
            mainFragment.refreshUI();
        }
    }

    public static void reFreshData(){
        if(messageFragment != null || mainFragment != null){
            messageFragment.refrshData();
            mainFragment.refrshData();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message,null);
        DataHolder holder = new DataHolder(view, parent.getContext());
        sharedPreferences = parent.getContext().getSharedPreferences("goodData",Context.MODE_PRIVATE);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final DataHolder dataHolder = (DataHolder)holder;

        if(tieZis.get(position).getUserNickName().equals("")){
            dataHolder.nameTextView.setText(tieZis.get(position).getUserID());
        }else{
            dataHolder.nameTextView.setText(tieZis.get(position).getUserNickName());
        }

        if(tieZis.get(position).getPicString().equals("")){
            dataHolder.headImageView.setBackgroundResource(R.drawable.de_head);
        }else {
            dataHolder.headImageView.setBackground(new BitmapDrawable(BitmapAndStringUtils.StringToBitmap(tieZis.get(position).getPicString())));
        }

        Log.i("cydLog",position + " " + tieZis.get(position).getTieZiId());
        if(sharedPreferences.getBoolean(tieZis.get(position).getTieZiId(),false)){
            dataHolder.goodCheckBox.setChecked(true);
            if(!watchTieZis.contains( tieZis.get(position) ))
                watchTieZis.add(tieZis.get(position));
        }else{
            dataHolder.goodCheckBox.setChecked(false);
        }
        dataHolder.titleTextView.setText(tieZis.get(position).getTitle());
        dataHolder.contentTextView.setText(tieZis.get(position).getContext());
        if(tieZis.get(position).getImage1().equals("")){
            dataHolder.contentImageView.setVisibility(View.GONE);
        }else {
            dataHolder.contentImageView.setImageBitmap(BitmapAndStringUtils.StringToBitmap(tieZis.get(position).getImage1()));
        }

        dataHolder.browseTextView.setText("已阅"+tieZis.get(position).getWatchCount());

        dataHolder.timeTextView.setText(tieZis.get(position).getFirstTime().substring(5, 16 ));

        dataHolder.contentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!UserInfo.userID.equals(""))//True表示登录成功
                {
                    //访问服务器 已阅量+1
                    String dataUrl = NetData.urlReadNote;
                    RequestQueue queue = NoHttp.newRequestQueue();
                    Request<String> request = NoHttp.createStringRequest(dataUrl, RequestMethod.POST);

                    final JSONObject jsonObject = new JSONObject();
                    try {
                        //在这里面写请求内容，转化为json格式
                        jsonObject.put("Seen", tieZis.get(position).getTieZiId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    request.setDefineRequestBodyForJson(jsonObject);
                    queue.add(0, request, new OnResponseListener<String>() {
                        @Override
                        public void onStart(int what) {

                        }

                        @Override
                        public void onSucceed(int what, Response<String> response) {
                            //已阅成功不做处理
                        }

                        @Override
                        public void onFailed(int what, Response<String> response) {

                        }

                        @Override
                        public void onFinish(int what) {

                        }
                    });

                    //Toast.makeText(dataHolder.itemView.getContext(), tieZis.get(position).getUserID() + "好像被点了", Toast.LENGTH_SHORT).show();
                    String tieziID = tieZis.get(position).getTieZiId();
                    Intent intent = new Intent(dataHolder.itemView.getContext(), LookTieActivity.class);
                    intent.putExtra("tieID",tieziID);
                    mainActivity.startActivity(intent);
                } else {
                    GoIntent goIntent = new GoIntent(dataHolder.itemView.getContext());
                    goIntent.jump();//此处要求用户登录
                }
            }
        });

        dataHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!UserInfo.userID.equals(""))//True表示登录成功
                {
                    //访问服务器 已阅量+1
                    String dataUrl = NetData.urlReadNote;
                    RequestQueue queue = NoHttp.newRequestQueue();
                    Request<String> request = NoHttp.createStringRequest(dataUrl, RequestMethod.POST);

                    final JSONObject jsonObject = new JSONObject();
                    try {
                        //在这里面写请求内容，转化为json格式
                        jsonObject.put("Seen", tieZis.get(position).getTieZiId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    request.setDefineRequestBodyForJson(jsonObject);
                    queue.add(0, request, new OnResponseListener<String>() {
                        @Override
                        public void onStart(int what) {

                        }

                        @Override
                        public void onSucceed(int what, Response<String> response) {
                            //已阅成功不做处理
                        }

                        @Override
                        public void onFailed(int what, Response<String> response) {

                        }

                        @Override
                        public void onFinish(int what) {

                        }
                    });

                    Toast.makeText(dataHolder.itemView.getContext(), "正在加载……", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(dataHolder.itemView.getContext(), tieZis.get(position).getUserID() + "好像被点了", Toast.LENGTH_SHORT).show();
                    String tieziID = tieZis.get(position).getTieZiId();
                    Intent intent = new Intent(dataHolder.itemView.getContext(), LookTieActivity.class);
                    intent.putExtra("tieID",tieziID);
                    mainActivity.startActivity(intent);
                } else {
                    GoIntent goIntent = new GoIntent(dataHolder.itemView.getContext());
                    goIntent.jump();//此处要求用户登录
                }
            }
        });

        dataHolder.goodCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dataHolder.goodCheckBox.isChecked()){
                    //Toast.makeText(dataHolder.itemView.getContext(), position+"点赞+1", Toast.LENGTH_SHORT).show();

                    //添加到已阅
//                    if(!watchTieZis.contains( tieZis.get(position) ))
//                        watchTieZis.add(tieZis.get(position));

                    reFreshUI();

                    editor = sharedPreferences.edit();
                    editor.putBoolean(tieZis.get(position).getTieZiId(), true);

                    //访问服务器 点赞量+1
                    String dataUrl = NetData.urlThumbUp;
                    RequestQueue queue = NoHttp.newRequestQueue();
                    Request<String> request = NoHttp.createStringRequest(dataUrl, RequestMethod.POST);

                    final JSONObject jsonObject = new JSONObject();
                    try {
                        //在这里面写请求内容，转化为json格式
                        jsonObject.put("Plus", tieZis.get(position).getTieZiId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    request.setDefineRequestBodyForJson(jsonObject);
                    queue.add(0, request, new OnResponseListener<String>() {
                        @Override
                        public void onStart(int what) {

                        }

                        @Override
                        public void onSucceed(int what, Response<String> response) {
                            //点赞成功不做处理
                        }

                        @Override
                        public void onFailed(int what, Response<String> response) {

                        }

                        @Override
                        public void onFinish(int what) {

                        }
                    });



                }
                else{
                    //Toast.makeText(dataHolder.itemView.getContext(), position+"取消点赞", Toast.LENGTH_SHORT).show();

                    reFreshUI();

                    editor = sharedPreferences.edit();
                    editor.putBoolean(tieZis.get(position).getTieZiId(), false);
                }
                editor.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(null == tieZis) return 0;
        return tieZis.size();
    }

    class DataHolder extends RecyclerView.ViewHolder {

        //item top
        ImageView headImageView;
        TextView nameTextView;
        CheckBox goodCheckBox;

        TextView titleTextView;
        TextView contentTextView;
        ImageView contentImageView;

        TextView browseTextView;
        TextView timeTextView;

        //
        LinearLayout l123;

        public DataHolder(final View itemView, Context context) {
            super(itemView);

            headImageView = itemView.findViewById(R.id.head_imageview_item_message);
            nameTextView = itemView.findViewById(R.id.name_textview_item_message);
            goodCheckBox = itemView.findViewById(R.id.good_checkbox_item_message);

            titleTextView = itemView.findViewById(R.id.title_textview_item_message);
            contentTextView = itemView.findViewById(R.id.content_textview_item_message);
            contentImageView = itemView.findViewById(R.id.content_imageview_item_message);

            browseTextView = itemView.findViewById(R.id.browse_textview_item_message);
            timeTextView = itemView.findViewById(R.id.time_textview_item_message);

//            l123 = itemView.findViewById(R.id.L1234);

            Drawable[] draid = goodCheckBox.getCompoundDrawables();
            //setBounds (1,2,3,4) 1是距左右边距离,2是距上下边距离,3是长度,4是宽度
            draid[0].setBounds(0, 0, context.getResources().getDimensionPixelSize(R.dimen.good_checkbox_height),
                    context.getResources().getDimensionPixelSize(R.dimen.good_checkbox_width));
            goodCheckBox.setCompoundDrawables(draid[0], draid[1], draid[2], draid[3]);


        }
    }
}
