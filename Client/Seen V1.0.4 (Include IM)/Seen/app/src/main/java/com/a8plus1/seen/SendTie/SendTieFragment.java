package com.a8plus1.seen.SendTie;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.a8plus1.seen.Adapter.MessageRecyclerAdapter;
import com.a8plus1.seen.Bean.NetData;
import com.a8plus1.seen.Bean.UserInfo;
import com.a8plus1.seen.MainActivity;
import com.a8plus1.seen.R;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.StringRequest;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static android.app.Activity.RESULT_OK;


public class SendTieFragment extends Fragment {    //发送帖子界面
    @Nullable
    private EditText editText,editText2;
    private String content,tital,localtime;
    private ImageView send,back;
    private ImageView[] tu = new ImageView[4];

    //上传图片
    private ImageView test;
    private static final int REQUEST_CODE_CHOOSE = 23;
    Bitmap[] image = new Bitmap[4];
    int imagecount;

    //计算时长
    Long time;
    Date date;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frament_sendtie,container,false);
        initSendTieFragment(view);
        imagecount = 0;
        image[1] = null;
        image[2] = null;
        image[3] = null;
        return view;
    }

    private void initSendTieFragment(View view) {
        send = view.findViewById(R.id.send);//发送数据的按钮
        back = view.findViewById(R.id.back);//返回的按钮
        editText = view.findViewById(R.id.context);
        editText2 = view.findViewById(R.id.tital);
        test = view.findViewById(R.id.tu);
        tu[1] = view.findViewById(R.id.tu1);
        tu[2] = view.findViewById(R.id.tu2);
        tu[3] = view.findViewById(R.id.tu3);
        initListener();
    }

    private void initListener() {
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(imagecount >= 3)) {
                    RxPermissions rxPermissions = new RxPermissions(getActivity());
                    rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe(new Observer<Boolean>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(Boolean aBoolean) {
                                    if (aBoolean) {
                                        Matisse.from(SendTieFragment.this)
                                                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG, MimeType.GIF))
                                                .theme(R.style.Matisse_Dracula)
                                                .maxSelectable(1)
                                                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                                .thumbnailScale(0.85f)
                                                .imageEngine(new PicassoEngine())
                                                .forResult(REQUEST_CODE_CHOOSE);
                                    } else {
                                        Toast.makeText(getActivity(), R.string.permission_request_denied, Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                } else {
                    Toast.makeText(getActivity(), "最多上传三张图片！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //发送数据按钮按钮
                content = editText.getText().toString();//内容
                tital = editText2.getText().toString();//标题
                SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyy-MM-dd hh:mm:ss");
                Date curDate =  new Date(System.currentTimeMillis());
                localtime=formatter.format(curDate);//获取本地时间
                if(!(tital.equals("") || content.equals(""))){
                    sendResultWithNohttp();
                } else {
                    Toast.makeText(getActivity(), "标题或内容不能为空!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //返回按钮按钮  返回朕的天下
                getActivity().finish();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            imagecount++;
            Uri mImageCaptureUri = Matisse.obtainResult(data).get(0);
            Matisse.obtainResult(data).remove(0);
            if (mImageCaptureUri != null) {
                try {
                    image[imagecount] = getBitmapFormUri(getActivity(), mImageCaptureUri);
                    tu[imagecount].setImageBitmap(image[imagecount]);
                }catch(Exception e) {
                    Toast.makeText(getActivity(), "找不到图片", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public static Bitmap getBitmapFormUri(Activity ac, Uri uri) throws FileNotFoundException, IOException {//通过uri获取图片并进行压缩
        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //图片分辨率以480x800为标准
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return compressImage(bitmap);//再进行质量压缩
    }

    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 20) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    private void sendResultWithNohttp() {//发送请求
        Request<String> request =  new StringRequest(NetData.urlNote, RequestMethod.POST);
        JSONObject jsonObject = new JSONObject();
        try {
            //在这里面写请求内容，转化为json格式
            jsonObject.put("content", content);
            jsonObject.put("t_userID", UserInfo.userID);
            jsonObject.put("time",localtime);
            jsonObject.put("title",tital);
            if(image[1] != null){
                jsonObject.put("Image1",BitmapAndStringUtils.convertIconToString(image[1]));
            } else {
                jsonObject.put("Image1","");
            }
            if(image[2] != null){
                jsonObject.put("Image2",BitmapAndStringUtils.convertIconToString(image[2]));
            } else {
                jsonObject.put("Image2","");
            }
            if(image[3] != null){
                jsonObject.put("Image3",BitmapAndStringUtils.convertIconToString(image[3]));
            } else {
                jsonObject.put("Image3","");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setDefineRequestBodyForJson(jsonObject);
        RequestQueue queue = NoHttp.newRequestQueue();
        queue.add(0, request, new OnResponseListener<String>(){

            @Override
            public void onStart(int what) {
                System.out.println("[ProSS]下面展示发帖信息：");
                System.out.println("发帖人:" + UserInfo.userID);
                System.out.println("标题:" + tital);
                System.out.println("内容:" + content);
                System.out.println("时间:" + localtime);
                if(image[1] != null){
                    System.out.println("image1:" + BitmapAndStringUtils.convertIconToString(image[1]).substring(BitmapAndStringUtils.convertIconToString(image[1]).length() - 20));
                    System.out.println("所占空间:" + BitmapAndStringUtils.convertIconToString(image[1]).getBytes().length);
                } else {
                    System.out.println("image1:");
                }
                if(image[2] != null){
                    System.out.println("image2:" + BitmapAndStringUtils.convertIconToString(image[2]).substring(BitmapAndStringUtils.convertIconToString(image[2]).length() - 20));
                    System.out.println("所占空间:" + BitmapAndStringUtils.convertIconToString(image[2]).getBytes().length);
                } else {
                    System.out.println("image2:");
                }
                if(image[3] != null){
                    System.out.println("image3:" + BitmapAndStringUtils.convertIconToString(image[3]).substring(BitmapAndStringUtils.convertIconToString(image[3]).length() - 20));
                    System.out.println("所占空间:" + BitmapAndStringUtils.convertIconToString(image[3]).getBytes().length);
                } else {
                    System.out.println("image3:");
                }
                date = new Date();
                time= date.getTime();
            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                if(response.responseCode() == 200){
                    Toast.makeText(getActivity(), "下旨成功！", Toast.LENGTH_SHORT).show();
                    MessageRecyclerAdapter.reFreshData();
                    getActivity().finish();
                }else{
                    Toast.makeText(getActivity(), "下旨失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                System.out.println("发帖超时");
            }

            @Override
            public void onFinish(int what) {
                date = new Date();
                time= date.getTime() - time;
                System.out.println("[ProSS]发帖用时" + time + "毫秒");
            }
        });
    }
}