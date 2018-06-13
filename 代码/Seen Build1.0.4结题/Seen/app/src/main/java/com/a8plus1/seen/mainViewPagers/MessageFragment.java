package com.a8plus1.seen.mainViewPagers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.a8plus1.seen.Adapter.MessageRecyclerAdapter;
import com.a8plus1.seen.Bean.NetData;
import com.a8plus1.seen.Bean.UserInfo;
import com.a8plus1.seen.R;
import com.a8plus1.seen.SendTie.SendActivity;
import com.a8plus1.seen.TieZi;
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


public class MessageFragment extends Fragment {

    private RecyclerView messageRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MessageRecyclerAdapter messageRecyclerViewAdapter;
    private ImageView addTieZiImageView;

    ArrayList<TieZi> tieZis = new ArrayList<>();

    //存帖子信息用集合
    String[] tieZiInfosTemp;
    //统计帖子数 工具变量
    int num = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        initMessageFragmentView(view);

        return view;
    }

    //初始化视图
    private void initMessageFragmentView(final View view) {

        messageRecyclerView = view.findViewById(R.id.message_recyclerview_messagefragment);
        initRecyclerView();

        swipeRefreshLayout = view.findViewById(R.id.refresh_swiperefresh_fragment_message);
        initSwipRefreshLayout();
        //设置刷新控件动画中的颜色。参数为资源id

        addTieZiImageView = view.findViewById(R.id.addTiezi_imageview_message_fragment);
        iniAddTieZiImageView();

    }

    private void iniAddTieZiImageView() {
        addTieZiImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!UserInfo.userID.equals(""))
                {
                    Intent intent = new Intent(getContext(), SendActivity.class);
                    getActivity().startActivity(intent);
                } else {
                    GoIntent goIntent = new GoIntent(getContext());
                    goIntent.jump();
                    return;
                }
            }
        });
    }


    //初始化RecyclerView
    private void initRecyclerView() {
        messageRecyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 1, GridLayoutManager.VERTICAL, false));
        initData();
        messageRecyclerViewAdapter = new MessageRecyclerAdapter(tieZis, this.getActivity());
        messageRecyclerView.setAdapter(messageRecyclerViewAdapter);
    }

    //从服务器中获取数据
    private void initData() {
        //存放帖子id
        String[] tieIds;

        String dataUrl = NetData.urlToHNote;
        //String dataUrl = "http://doc.nohttp.net/222880";
        RequestQueue queue = NoHttp.newRequestQueue();
        Request<String> request = NoHttp.createStringRequest(dataUrl, RequestMethod.POST);

        final JSONObject jsonObject = new JSONObject();
        try {
            //在这里面写请求内容，转化为json格式
            jsonObject.put("Sort", 1);
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

                                                    // public TieZi(String tieZiId, String userID, String userNickName, String title, String context, String picString, int watchCount, int goodCount, String firstTime) {
                                                    tieZis.add(new TieZi(
                                                            ((JSONObject)jsonArray.get(i)).getString("tieID"),
                                                            jsonObjectTemp.optString("t_userID", ""),
                                                            jsonObjectTemp.optString("nickname", ""),
                                                            jsonObjectTemp.optString("title",""),
                                                            jsonObjectTemp.optString("content",""),
                                                            jsonObjectTemp.optString("circleImage",""),
                                                            jsonObjectTemp.optInt("pageviews",0),
                                                            jsonObjectTemp.optInt("agree", 0),
                                                            jsonObjectTemp.optString("time",""),
                                                            jsonObjectTemp.optString("Image1","")
                                                    ));
                                                    messageRecyclerViewAdapter.notifyDataSetChanged();
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

    //初始化刷新控件
    private void initSwipRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(R.color.color1);
        //用于设置我们的下拉刷新回调事件，很重要的一个方法。
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                messageRecyclerViewAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void refreshUI(){
        messageRecyclerViewAdapter.notifyDataSetChanged();
    }

    public void refrshData(){
        initData();
    }

}
