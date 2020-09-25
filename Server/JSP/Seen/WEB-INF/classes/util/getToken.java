package util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import net.sf.json.JSONObject;

public class getToken {
	public String token="";
	JSONObject jsonObject = null;
	StringBuffer buffer = new StringBuffer();

	public getToken(String userId,String name) {
        String uri = "";

        long t = System.currentTimeMillis() / 1000;
        String time = String.valueOf(t);//以上为获取时间戳

        String ak = "x4vkb1qpxfwxk";//app key
        String Nonce = "";//
        Random rand = new Random();
        Nonce = Integer.toString(rand.nextInt(10000));

        String Signature = shaEncrypt("3Lkmoq7hts8Q" + Nonce + time);//计算校验码
      
        try {
            URL url = new URL("http://api.cn.ronghub.com/user/getToken.json");
            // 将url 以 open方法返回的urlConnection  连接强转为HttpURLConnection连接  (标识一个url所引用的远程对象连接)
            // 此时cnnection只是为一个连接对象,待连接中
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置连接输出流为true,默认false (post 请求是以流的方式隐式的传递参数)
            connection.setDoOutput(true);
            // 设置连接输入流为true
            connection.setDoInput(true);
            // 设置请求方式为post
            connection.setRequestMethod("POST");
            // post请求缓存设为false
            connection.setUseCaches(false);
            // 设置该HttpURLConnection实例是否自动执行重定向
            connection.setInstanceFollowRedirects(true);
            // 设置请求头里面的各个属性 (以下为设置内容的类型,设置为经过urlEncoded编码过的from参数)
            // application/x-javascript text/xml->xml数据 application/x-javascript->json对象 application/x-www-form-urlencoded->表单数据
            // ;charset=utf-8 必须要
            //addRequestProperty添加相同的key不会覆盖，如果相同，内容会以{name1,name2}
            //connection.addRequestProperty("from", "sfzh");  //来源哪个系统
            //setRequestProperty添加相同的key会覆盖value信息
            //setRequestProperty方法，如果key存在，则覆盖；不存在，直接添加。
            //addRequestProperty方法，不管key存在不存在，直接添加。
            //connection.setRequestProperty("user", "user");  //访问申请用户
            //InetAddress address = InetAddress.getLocalHost();
            //String ip=address.getHostAddress();//获得本机IP
            //connection.setRequestProperty("ip",ip);  //请求来源IP
            //connection.setRequestProperty("encry", "123456");

            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("APP-Key", ak);
            connection.setRequestProperty("Nonce", Nonce);
            connection.setRequestProperty("Timestamp", time);
            connection.setRequestProperty("Signature", Signature);

            // 建立连接 (请求未开始,直到connection.getInputStream()方法调用时才发起,以上各个参数设置需在此方法之前进行)
            connection.connect();
            // 创建输入输出流,用于往连接里面输出携带的参数,(输出内容为?后面的内容)
            DataOutputStream dataout = new DataOutputStream(connection.getOutputStream());
            // 格式 parm = aaa=111&bbb=222&ccc=333&ddd=444
            String parm = "userId=" + userId + "&name=" + name + "&portraitUri=" + uri;
            System.out.println("传递参数：" + parm);
            // 将参数输出到连接
            dataout.writeBytes(parm);
            // 输出完成后刷新并关闭流
            dataout.flush();
            dataout.close(); // 重要且易忽略步骤 (关闭流,切记!)
            //System.out.println(connection.getResponseCode());
            // 连接发起请求,处理服务器响应  (从连接获取到输入流并包装为bufferedReader)
            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            connection.disconnect();
            jsonObject = JSONObject.fromObject(buffer.toString());
            System.out.println("!!!获得了!!!!!!!!!!!!!!!!!!"+jsonObject.get("token"));
         
           
            token =jsonObject.getString("token");
           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	public String shaEncrypt(String strSrc) {//SHA-1加密算法
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = strSrc.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-1");// 将此换成SHA-1、SHA-512、SHA-384等参数
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    public String bytes2Hex(byte[] bts) {//同加密
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }
}
