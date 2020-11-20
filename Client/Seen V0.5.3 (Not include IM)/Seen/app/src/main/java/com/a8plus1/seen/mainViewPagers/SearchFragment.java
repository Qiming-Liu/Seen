package com.a8plus1.seen.mainViewPagers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.a8plus1.seen.Adapter.MessageRecyclerAdapter;
import com.a8plus1.seen.Bean.NetData;
import com.a8plus1.seen.MainActivity;
import com.a8plus1.seen.R;
import com.a8plus1.seen.TieZi;
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


public class SearchFragment extends Fragment {

    private RecyclerView messageRecyclerView;
    private MessageRecyclerAdapter searchRecyclerViewAdapter;

    private EditText searchTextEditText;
    private Button searchButton;

    ArrayList<TieZi> tieZis = new ArrayList<>();

    //存帖子信息用集合
    String[] tieZiInfosTemp;
    //统计帖子数 工具变量
    int num = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        initSearchFragmentView(view);

        return view;
    }


    private void initSearchFragmentView(View view) {

        messageRecyclerView = (RecyclerView) view.findViewById(R.id.search_recyclerview_searchfragment);
        initRecyclerView();

        searchTextEditText = view.findViewById(R.id.searchtext_edittext_searchfragment);
        searchButton = view.findViewById(R.id.search_button_searchfragment);
        initSearchListener();

    }

    private void initSearchListener() {
        searchButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    String keyString = searchTextEditText.getText().toString();
                    searchButton.setBackgroundResource(R.color.colorPrimaryDark);
                    if(keyString.equals("")){
                        Toast.makeText(getContext(), "请输入关键字", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    //从服务器中load数据 再进行筛选******
                    initData();

//                ArrayList<TieZi> temps = new ArrayList<>();
//                for(int i = 0 ; i < tieZis.size(); i ++) {
//                    temps.add(tieZis.get(i));
//                }
//                tieZis.removeAll(tieZis);

//                for(int i = 0; i < temps .size(); i ++ ){
//                    //如果不是子串则删除……
//                    String temp = temps.get(i).getTitle();
//                    if(temp.contains(keyString)){
//                        tieZis.add(temps.get(i));
//                    }
//                }
                    String dataUrl = NetData.urlFindNote;
                    RequestQueue queue = NoHttp.newRequestQueue();
                    Request<String> request = NoHttp.createStringRequest(dataUrl, RequestMethod.POST);
                    //request.add("Search", keyString);
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("Search", keyString);
                        request.setDefineRequestBodyForJson(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    queue.add(2, request, new OnResponseListener<String>() {
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
//                                                            String moniData = "{\"t_userID\": \"a123\",\"title\": \"929\",\"content\": \"qqqq\",\"time\": \"2017-09-29 06:59:46.0\",\"circleImage\": \"江汉大学\",\"nickname\": \"-115.936318\",\"agree\": 2,\"pageviews\": 1}";
//                                                            JSONObject jsonObjectTemp = null;
//                                                            jsonObjectTemp = new JSONObject(moniData);

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
                                                                searchRecyclerViewAdapter.notifyDataSetChanged();
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
                            }else {
                                Log.i("cyd000", "connect failed");
                            }
                        }

                        @Override
                        public void onFailed(int what, Response<String> response) {

                        }

                        @Override
                        public void onFinish(int what) {

                        }
                    });

                    searchRecyclerViewAdapter = new MessageRecyclerAdapter(tieZis, getActivity());
                    messageRecyclerView.setAdapter(searchRecyclerViewAdapter);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    searchButton.setBackgroundResource(R.color.clickChuanZhao);
                }
                return true;
            }
        });

    }

    private void initRecyclerView() {
        messageRecyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 1, GridLayoutManager.VERTICAL, false));
    }

    private void initData() {
        if(!tieZis.isEmpty()) tieZis.removeAll(tieZis);
        //获得数据应为一个JsonArray

        //测试数据
//        for(int i = 0; i < 20; i ++){
//            tieZis.add(new TieZi("0000000" + i, "000000"+i, "鸡蛋",i+"为新时代中国特色",
//                    "新华社北京电 题：为新时代中国特色社会主义提供有力宪法保障——各地干部群众热议党的十九届二中全会公报\n" +
//                            "\n" +
//                            "　　新华社记者\n" +
//                            "\n" +
//                            "　　“宪法修改是国家政治生活中的一件大事，是党中央从新时代坚持和发展中国特色社会主义全局和战略高度作出的重大决策，也是推进全面依法治国、推进国家治理体系和治理能力现代化的重大举措。”\n" +
//                            "\n" +
//                            "　　治国凭圭臬，安邦靠准绳。19日发布的党的十九届二中全会公报，在社会各界引起高度关注和强烈反响。\n" +
//                            "\n" +
//                            "　　“公报凝聚共识，顺应时代要求和人民意愿。”广大干部群众表示，要更加紧密地团结在以习近平同志为核心的党中央周围，以习近平新时代中国特色社会主义思想为指导，认真贯彻落实党的十九大精神，坚定不移走中国特色社会主义法治道路，自觉维护宪法权威、保证宪法实施，为新时代推进全面依法治国、建设社会主义法治国家而努力奋斗。"
//                    ,1234, 888,"2007-1-12"));
//        }
    }

}
