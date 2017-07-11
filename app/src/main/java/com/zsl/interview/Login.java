package com.zsl.interview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zsl on 2017/6/28.
 */
public class Login extends Activity implements View.OnClickListener {
    private ImageView imageView;
    private TextView phone,pwd;
    private Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        init();

    }

    private void init() {
        imageView = (ImageView) findViewById(R.id.close);
        phone = (TextView) findViewById(R.id.phone);
        pwd = (TextView) findViewById(R.id.pwd);
        login = (Button) findViewById(R.id.login);

        imageView.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.close:
                Intent intent = new Intent(Login.this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.login:
                getdata();
                break;
        }
    }

    public void getdata() {
        String username = phone.getText().toString();
        String password = stringToMD5(pwd.getText().toString());

        OkHttpClient okhttpclient  =  new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        Request request = builder.get().url("http://interview.jbangit.com/user/login?username="+username+"&password="+password+"").build();
        Call call = okhttpclient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String result = response.body().string();
                try {
                    JSONObject jb = new JSONObject(result);
                    int code = jb.getInt("code");
                    if(code ==0){
                        Intent intent = new Intent(Login.this,My.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public static String stringToMD5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }
}
