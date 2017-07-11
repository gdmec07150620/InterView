package com.zsl.interview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zsl on 2017/6/27.
 */
public class MyHttpUnit {


    public List<Map<String,Object>> getData(String result){
         List<Map<String,Object>> getdata = new ArrayList<Map<String,Object>>();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    String message = jsonObject.getString("message");
                    int page = jsonObject.getInt("page");
                    int page_size = jsonObject.getInt("page_size");
                    int total_count = jsonObject.getInt("total_count");
                    if(code==0){
                        for(int i=0;i<total_count;i++){
                            Map<String,Object>map = new HashMap<String, Object>();
                            JSONArray resultJsonArray = jsonObject.getJSONArray("data");
                            JSONObject resultJsonObject = resultJsonArray.getJSONObject(i);
                            int vote_count = resultJsonObject.getInt("vote_count");
                            int reply_count=  resultJsonObject.getInt("reply_count");
                            String  title=   resultJsonObject.getString("title");
                            String  desc= resultJsonObject.getString("desc");
                            String  image=resultJsonObject.getString("image");
                            String  post_time=resultJsonObject.getString("post_time");

                            map.put("title",title);
                            map.put("desc",desc);
                            map.put("image",getInternetPicture(image));
                            getdata.add(map);

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        return getdata;
    }
    public Bitmap getInternetPicture(String UrlPath) {
        Bitmap bm = null;
        String urlpath = UrlPath;
        // 2、获取Uri
        try {
            URL uri = new URL(urlpath);
            // 3、获取连接对象、此时还没有建立连接
            HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
            // 4、初始化连接对象
            // 设置请求的方法，注意大写
            connection.setRequestMethod("GET");
            // 读取超时
            connection.setReadTimeout(2000);
            // 设置连接超时
            connection.setConnectTimeout(5000);
            // 5、建立连接
            connection.connect();

            // 6、获取成功判断,获取响应码
            if (connection.getResponseCode() == 200) {
                // 7、拿到服务器返回的流，客户端请求的数据，就保存在流当中
                InputStream is = connection.getInputStream();
                // 8、从流中读取数据，构造一个图片对象GoogleAPI
                bm = BitmapFactory.decodeStream(is);
                // 9、把图片设置到UI主线程
                // ImageView中,获取网络资源是耗时操作需放在子线程中进行,通过创建消息发送消息给主线程刷新控件；
            } else {
                bm = null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bm;
    }
}
