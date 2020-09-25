package com.a8plus1.seen.mainViewPagers;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a8plus1.seen.Adapter.MessageRecyclerAdapter;
import com.a8plus1.seen.Bean.NetData;
import com.a8plus1.seen.Bean.UserInfo;
import com.a8plus1.seen.R;
import com.a8plus1.seen.TieZi;
import com.a8plus1.seen.Utils.BitmapAndStringUtils;
import com.a8plus1.seen.Utils.GoIntent;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ZhenFaBuDeFragment extends Fragment {

    private RecyclerView messageRecyclerView;
    private MessageRecyclerAdapter recyclerViewAdapter;


    //返回
    private ImageView backImageView;
    //头像
    private ImageView headImageView;
    //userName
    private TextView userNameTextView;
    //qianming
    private TextView qianmingTextView;

    ArrayList<TieZi> tieZis = new ArrayList<>();
    //存帖子信息用集合
    String[] tieZiInfosTemp;
    //统计帖子数 工具变量
    int num = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zhenfabude, container, false);

        initZhenFaBuDeFragmentView(view);
        
        return view;
    }

    private void initZhenFaBuDeFragmentView(View view) {
        messageRecyclerView = (RecyclerView) view.findViewById(R.id.message_recyclerview_zhenfabude_fragment);
        initRecyclerView();

        backImageView = view.findViewById(R.id.back_imageview_zhenfabulde_fragment);
        InitBackImageView();

        headImageView = view.findViewById(R.id.head_imageview_zhenfabude);
        if(!UserInfo.headImage.equals("")){
            headImageView.setImageBitmap(BitmapAndStringUtils.StringToBitmap(UserInfo.headImage));
        }

        userNameTextView = view.findViewById(R.id.username_textview_zhenfabude);
        userNameTextView.setText(UserInfo.nickname);

        qianmingTextView = view.findViewById(R.id.qianming_textview_zhenfabule_fragment);
        qianmingTextView.setText(UserInfo.signature);

        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.up_linerarlayout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //留空
            }
        });
    }

    private void InitBackImageView() {
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "你按下了返回键", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        });
    }

    private void initRecyclerView() {
        messageRecyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 1, GridLayoutManager.VERTICAL, false));
        initData();
        recyclerViewAdapter = new MessageRecyclerAdapter(tieZis, this.getActivity());
        messageRecyclerView.setAdapter(recyclerViewAdapter);
    }

    private void initData() {
        //存放帖子id
        String[] tieIds;

        String dataUrl = NetData.urlOnesNote;
        //String dataUrl = "http://doc.nohttp.net/222880";
        RequestQueue queue = NoHttp.newRequestQueue();
        Request<String> request = NoHttp.createStringRequest(dataUrl, RequestMethod.POST);

        final JSONObject jsonObject = new JSONObject();
        try {
            //在这里面写请求内容，转化为json格式
            //********************************************************************这里要改当前用户的uerID
            if(!UserInfo.userID.equals(""))//如果userID是空的，表示用户未登陆
            {
                jsonObject.put("userID", UserInfo.userID);
            } else {
                //登陆失败要求用户登录
                GoIntent goIntent = new GoIntent(getContext());
                goIntent.jump();
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setDefineRequestBodyForJson(jsonObject);

        queue.add(1, request, new OnResponseListener<String>() {

            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                if(response.responseCode() == 200){
                    String result = response.get();
                    try {
                        final JSONArray jsonArray = new JSONArray(result);

                        //存所有帖子信息
                        tieZiInfosTemp = new String[jsonArray.length()];

                        for(int i = 999; i < jsonArray.length() + 999; i++){
                            //String id = ((JSONObject)jsonArray.get(i)) .getString("tieID");
                            //Request<String> request2 = NoHttp.createStringRequest(NetData.urlGetNote, RequestMethod.POST);
                            Request<String> request2 = NoHttp.createStringRequest(NetData.urlGetNote, RequestMethod.POST);
                            RequestQueue queue = NoHttp.newRequestQueue();
                            try {
                                request2.setDefineRequestBodyForJson((JSONObject)(jsonArray.get(i-999)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            queue.add(i, request2, new OnResponseListener<String>() {

                                @Override
                                public void onStart(int what) {
                                }

                                @Override
                                public void onSucceed(int what, Response<String> response) {
                                    num++;
                                    if(response.responseCode() == 200){
                                        tieZiInfosTemp[what-999] = response.get();
                                    }
                                    if(num == jsonArray.length() ){
                                        num = 0;
                                        //获得所有帖子数据之后 开始解析
                                        if(!tieZis.isEmpty()) tieZis.removeAll(tieZis);
                                        for(int i = 0; i < jsonArray.length(); i ++){
                                            if(!tieZiInfosTemp[i].equals("")){
                                                try {
                                                    JSONArray jsonObject1 = new JSONArray(tieZiInfosTemp[i]);
                                                    JSONObject jsonObjectTemp = null;
                                                    jsonObjectTemp = (JSONObject) jsonObject1.get(0);

                                                    //JSONObject jsonObject1 = new JSONObject(tieZiInfosTemp[i]);
                                                    // 数据有误!!!!!
                                                    //模拟Data
//                                                    String moniData = "{\"t_userID\": \"asdfasdf\",\"title\": \"asdfasdfasdf\",\"content\": \"qqwegfwgwqq\",\"time\": \"2017-09-29 06:59:46.0\",\"circleImage\": \"江汉大学\",\"nickname\": \"-115.936318\",\"agree\": 2,\"pageviews\": 1}";
//                                                    JSONObject jsonObjectTemp = null;
//                                                    jsonObjectTemp = new JSONObject(moniData);

                                                    // public TieZi(String tieZiId, String userID, String userNickName, String title, String context, String picString, int watchCount, int goodCount, String firstTime) {
                                                    tieZis.add(new TieZi(
                                                            ((JSONObject)jsonArray.get(i)).getString("tieID"),
                                                            jsonObjectTemp.getString("t_userID"),
                                                            jsonObjectTemp.getString("nickname"),
                                                            jsonObjectTemp.getString("title"),
                                                            jsonObjectTemp.getString("content"),
                                                            jsonObjectTemp.getString("circleImage"),
                                                            jsonObjectTemp.getInt("pageviews"),
                                                            jsonObjectTemp.getInt("agree"),
                                                            jsonObjectTemp.getString("time"),
                                                            jsonObjectTemp.optString("Image1","")
                                                    ));
                                                    recyclerViewAdapter.notifyDataSetChanged();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onFailed(int what, Response<String> response) {
                                }

                                @Override
                                public void onFinish(int what) {
                                }
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.i("cyd111",result);
                }else {
                    Log.i("cyd222", "connect failed");
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });

   }

}
