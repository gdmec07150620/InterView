package com.zsl.interview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {
    private ListView listView;
    public static String apkPackageUrl = "http://interview.jbangit.com/news";
    private SimpleAdapter simpleAdapter;
    private TextView textView;

    private SwipeRefreshLayout mSwipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listview);

        //设置下拉刷新的样式
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwipeLayout.setDistanceToTriggerSync(400);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        mSwipeLayout.setProgressBackgroundColor(R.color.red); // 设定下拉圆圈的背景
        mSwipeLayout.setSize(SwipeRefreshLayout.LARGE); // 设置圆圈的大小


        textView = (TextView) findViewById(R.id.user);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Login.class);
                startActivity(intent);
            }
        });
        OkHttpClient okHttpClient = new OkHttpClient();
                final Request request = new Request.Builder().url(apkPackageUrl).build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        Looper.prepare();
                        Toast.makeText(MainActivity.this, "网络连接异常", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                    @Override
                    public void onResponse(Response response) throws IOException {
                        String result = response.body().string();
                       MyHttpUnit myHttpUnit = new MyHttpUnit();
                        Message message = new Message();
                        message.obj=myHttpUnit.getData(result);
                        handler.sendMessage(message);
                    }
                });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            simpleAdapter = new SimpleAdapter(MainActivity.this,
                    (List<? extends Map<String, ?>>) msg.obj,
                    R.layout.item,new String[]{"title","desc","image"},
                    new int[]{R.id.biaoti,R.id.neirong,R.id.img});

            simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data, String textRepresentation) {
                    if (view instanceof ImageView && data instanceof Bitmap) {
                        ImageView iv = (ImageView) view;
                        iv.setImageBitmap((Bitmap) data);
                        return true;
                    }
                    return false;
                }
            });
            listView.setAdapter(simpleAdapter);
        }
    };

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 停止刷新
                mSwipeLayout.setRefreshing(false);
            }
        }, 4000); // 5秒后发送消息，停止刷新
    }
}
